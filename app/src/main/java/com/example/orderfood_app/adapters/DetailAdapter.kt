package com.example.orderfood_app.adapters

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

class DetailAdapter (
    private val product : ArrayList<Product>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailAdapter.DetailViewHolder, position: Int) { //hàm xử ký ráp sự kiện cho item
        val product = product[position]
        holder.productImage.load(product.image)
        holder.productName.text = product.name
        holder.productDescription.text = product.description
        holder.productPrice.text = product.price
        holder.productRating.rating = product.rating
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }
    }

    override fun getItemCount(): Int = product.size

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //gọi giá trị từng id trong 1 item
        val productImage: ImageView = itemView.findViewById<ImageView>(R.id.detail_image)
        val productName: TextView = itemView.findViewById<TextView>(R.id.detail_name)
        val productDescription: TextView = itemView.findViewById<TextView>(R.id.detail_description)
        val productPrice: TextView = itemView.findViewById<TextView>(R.id.detail_price)
        val productRating: RatingBar = itemView.findViewById<RatingBar>(R.id.detail_rating)
    }
}