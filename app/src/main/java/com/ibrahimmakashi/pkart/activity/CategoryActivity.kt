package com.ibrahimmakashi.pkart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat.getCategory
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ibrahimmakashi.pkart.R
import com.ibrahimmakashi.pkart.adapter.CategoryProductAdapter
import com.ibrahimmakashi.pkart.adapter.ProductAdapter
import com.ibrahimmakashi.pkart.model.AddProductModel
import java.util.ArrayList

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        getProducts(intent.getStringExtra("cate"))
    }

    private fun getProducts(category: String?) {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory", category)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                val recycyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recycyclerView.adapter = CategoryProductAdapter(this , list)
            }

    }
}