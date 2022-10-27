package com.ibrahimmakashi.pkart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat.getCategory
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ibrahimmakashi.pkart.R
import com.ibrahimmakashi.pkart.adapter.CategoryAdapter
import com.ibrahimmakashi.pkart.adapter.ProductAdapter
import com.ibrahimmakashi.pkart.databinding.FragmentHomeBinding
import com.ibrahimmakashi.pkart.model.AddProductModel
import com.ibrahimmakashi.pkart.model.CategoryModel
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.container.setOnRefreshListener {
            binding.container.isRefreshing = false

            getCategory()
            getSliderImage()
            getProducts()
        }


        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)

        if(preference.getBoolean("isCart", false))
            findNavController().navigate(R.id.action_homeFragment_to_cardFragment)


        getCategory()
        getSliderImage()
        getProducts()
        return binding.root
    }

    private fun getSliderImage() {
        Firebase.firestore.collection("slider").document("item")
            .get().addOnSuccessListener {
                Glide.with(requireContext()).load(it.get("img")).into(binding.sliderImage)
            }
    }

    private fun getProducts() {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    binding.progressBar3.visibility = VISIBLE
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                binding.progressBar3.visibility = GONE

                binding.productRecycler.adapter = ProductAdapter(requireContext(), list)
            }
    }

    private fun getCategory() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    binding.progressBar2.visibility = VISIBLE

                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.progressBar2.visibility = GONE

                binding.categoryRecycler.adapter = CategoryAdapter(requireContext(), list)
            }
    }

}