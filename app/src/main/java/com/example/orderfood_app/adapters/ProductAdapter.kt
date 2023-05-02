package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R


data class Product(val id: Int, val name: String, val image: Int, val loacation: String, val valueLocation: String)
class ProductAdapter(
    private val products: ArrayList<Product>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = products[position]
        holder.productImage.setImageResource(product.image)
        holder.productName.text = product.name
        holder.productLocation.text = product.loacation
        holder.productValueLocation.text = product.valueLocation
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = products.size


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById<ImageView>(R.id.product_image)
        val productName: TextView = itemView.findViewById<TextView>(R.id.product_name)
        val productLocation: TextView = itemView.findViewById<TextView>(R.id.location_product_lable)
        val productValueLocation: TextView = itemView.findViewById<TextView>(R.id.value_location)


    }

}