package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.orderfood_app.R
import com.example.orderfood_app.models.Product


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
        holder.productImage.load(product.image)
        holder.productName.text = product.name
        holder.productLocation.text = product.loacation
        holder.productDescription.text = product.description
        holder.productPrice.text = product.price
        holder.productRating.rating = product.rating
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = products.size


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById<ImageView>(R.id.product_image)
        val productName: TextView = itemView.findViewById<TextView>(R.id.product_name)
        val productLocation: TextView = itemView.findViewById<TextView>(R.id.location_product_lable)
        val productDescription: TextView = itemView.findViewById<TextView>(R.id.category_product)
        val productPrice: TextView = itemView.findViewById<TextView>(R.id.price)
        val productRating: RatingBar = itemView.findViewById<RatingBar>(R.id.product_ratingBar)


    }

}