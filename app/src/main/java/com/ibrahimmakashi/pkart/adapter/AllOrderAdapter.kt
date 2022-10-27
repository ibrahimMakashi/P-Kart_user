package com.ibrahimmakashi.pkart.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibrahimmakashi.pkart.activity.ProductDetailsActivity
import com.ibrahimmakashi.pkart.databinding.AllOrderItemLayoutBinding
import com.ibrahimmakashi.pkart.model.AllOrderModel

class AllOrderAdapter(val list : ArrayList<AllOrderModel>, val context: Context)
    : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {

    inner class AllOrderViewHolder(val binding : AllOrderItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(
            AllOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int ) {
        holder.binding.productTitle.text = list[position].name
        holder.binding.productPrice.text = list[position].price

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }

        when(list[position].status){
            "Ordered" -> {
              holder.binding.productStatus.text = "Ordered"
            }
            "Dispatched" -> {
                holder.binding.productStatus.text = "Dispatched"
                holder.binding.productStatus.setTextColor(Color.parseColor("#25AA07"))

            }
            "Delivered" -> {
                holder.binding.productStatus.text = "Delivered"
                holder.binding.productStatus.setTextColor(Color.parseColor("#25AA07"))

            }
            "Canceled" -> {
                holder.binding.productStatus.text = "Canceled"
                holder.binding.productStatus.setTextColor(Color.parseColor("#DF1919"))

            }
        }
    }



    override fun getItemCount(): Int {
        return list.size
    }
}