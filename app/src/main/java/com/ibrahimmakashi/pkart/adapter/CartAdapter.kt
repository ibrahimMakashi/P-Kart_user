package com.ibrahimmakashi.pkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibrahimmakashi.pkart.activity.ProductDetailsActivity
import com.ibrahimmakashi.pkart.databinding.LayoutCartItemBinding
import com.ibrahimmakashi.pkart.roomdb.AppDatabase
import com.ibrahimmakashi.pkart.roomdb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context : Context, val list : List<ProductModel>) :
    RecyclerView.Adapter<CartAdapter.CartVewHolder>() {

    inner class CartVewHolder(val binding : LayoutCartItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVewHolder {
        val binding = LayoutCartItemBinding.inflate(LayoutInflater.from(context),parent, false)
        return CartVewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartVewHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView4)

        holder.binding.textView11.text = list[position].productName
        holder.binding.textView12.text = list[position].productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }

        val dao = AppDatabase.getInstance(context).productDao()
        holder.binding.imageView5.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {

                dao.deleteProduct(
                    ProductModel(
                        list[position].productId,
                        list[position].productName,
                        list[position].productImage,
                        list[position].productSp

                    )
                )
            }
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }
}