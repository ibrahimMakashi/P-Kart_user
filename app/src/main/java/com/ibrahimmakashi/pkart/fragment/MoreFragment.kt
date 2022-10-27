package com.ibrahimmakashi.pkart.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ibrahimmakashi.pkart.R
import com.ibrahimmakashi.pkart.activity.ContactUsActivity
import com.ibrahimmakashi.pkart.activity.LoginActivity
import com.ibrahimmakashi.pkart.adapter.AllOrderAdapter
import com.ibrahimmakashi.pkart.databinding.FragmentMoreBinding
import com.ibrahimmakashi.pkart.model.AllOrderModel

class MoreFragment : Fragment() {

private lateinit var binding : FragmentMoreBinding
private lateinit var list : ArrayList<AllOrderModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(layoutInflater)

        list = ArrayList()

        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false

            getOrders()
        }

        binding.button7.setOnClickListener {
            startActivity(Intent(context, ContactUsActivity::class.java))
        }

        binding.button5.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            requireActivity().finishAffinity()
        }


        val preferences = requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
                binding.textView21.setText(it.getString("userName"))
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
            }

        Firebase.firestore.collection("allOrders")
            .whereEqualTo("userId", preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
            list.clear()
            for (doc in it){
                val data = doc.toObject(AllOrderModel::class.java)
                list.add(data)
            }
            binding.recyclerView.adapter = AllOrderAdapter(list, requireContext())
        }

        return binding.root
    }

    private fun getOrders(){
        val preferences = requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
                binding.textView21.setText(it.getString("userName"))
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
            }

        Firebase.firestore.collection("allOrders")
            .whereEqualTo("userId", preferences.getString("number", "")!!)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it){
                    val data = doc.toObject(AllOrderModel::class.java)
                    list.add(data)
                }
                binding.recyclerView.adapter = AllOrderAdapter(list, requireContext())
            }
    }


}