package com.ibrahimmakashi.pkart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ibrahimmakashi.pkart.R
import com.ibrahimmakashi.pkart.activity.AddressActivity
import com.ibrahimmakashi.pkart.activity.ProductDetailsActivity
import com.ibrahimmakashi.pkart.adapter.CartAdapter
import com.ibrahimmakashi.pkart.databinding.FragmentCardBinding
import com.ibrahimmakashi.pkart.roomdb.AppDatabase
import com.ibrahimmakashi.pkart.roomdb.ProductModel

class CardFragment : Fragment() {
    private lateinit var binding : FragmentCardBinding
    private lateinit var list : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardBinding.inflate(layoutInflater)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext()).productDao()

        list = ArrayList()

        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter = CartAdapter(requireContext(), it)

            list.clear()
            for(data in it){
                list.add(data.productId)
            }

            totalCost(it)
        }

        return binding.root
    }

    private fun totalCost(data: List<ProductModel>?) {

        var total =0
        for (item in data!!){
            total += item.productSp!!.toInt()

        }

        binding.textView13.text = "Total item in cart is ${data.size}"
        binding.textView14.text = "Total cost : $total"

        binding.checkout.setOnClickListener {
            if (data.size != 0) {
                val intent = Intent(context, AddressActivity::class.java)

                val b = Bundle()
                b.putStringArrayList("productIds", list)
                b.putString("totalCost", total.toString())
                intent.putExtras(b)
                startActivity(intent)
            }else{
                Toast.makeText(context, "Please select any Product.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}