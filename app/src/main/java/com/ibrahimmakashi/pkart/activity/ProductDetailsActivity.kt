package com.ibrahimmakashi.pkart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ibrahimmakashi.pkart.MainActivity
import com.ibrahimmakashi.pkart.R
import com.ibrahimmakashi.pkart.databinding.ActivityProductDetailsBinding
import com.ibrahimmakashi.pkart.roomdb.AppDatabase
import com.ibrahimmakashi.pkart.roomdb.ProductDao
import com.ibrahimmakashi.pkart.roomdb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getProductDetails(intent.getStringExtra("id"))
        binding.textView9.movementMethod = ScrollingMovementMethod()
    }

    private fun getProductDetails(proId: String?) {

        Firebase.firestore.collection("products")
            .document(proId!!).get().addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>
                val name = it.getString("productName")
                val productSp = it.getString("productSp")
                val productDesc = it.getString("productDescription")
                binding.textView7.text = name
                binding.textView8.text = productSp
                binding.textView9.text = productDesc

                val slideList = ArrayList<SlideModel>()
                for (data in list){
                    binding.progressBar4.visibility = VISIBLE

                    slideList.add(SlideModel(data, ScaleTypes.CENTER_INSIDE))
                }


                cartAction(proId, name , productSp, it.getString("productCoverImg"))
                binding.progressBar4.visibility = GONE

                binding.imageSlider.setImageList(slideList)

            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }

    private fun cartAction(proId: String, name: String?, productSp: String?, coverImg: String?) {

        val productDao = AppDatabase.getInstance(this).productDao()

        if (productDao.isExit(proId) != null){
            binding.textView10.text = "Go to Cart"
        }else{
            binding.textView10.text = "Add to Cart"
        }

        binding.textView10.setOnClickListener {
            if (productDao.isExit(proId) != null){
               openCart()
            }else{
              addToCart(productDao, proId, name, productSp, coverImg)
            }
        }

    }


    private fun addToCart(
        productDao: ProductDao,
        proId: String,
        name: String?,
        productSp: String?,
        coverImg: String?
    ) {
       val data = ProductModel(proId, name, coverImg, productSp)
        lifecycleScope.launch(Dispatchers.IO){
            productDao.insertProduct(data)
            binding.textView10.text = "Go to Cart"
        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}