package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R


data class ProductSearch(val id: Int, val name: String, val image: Int, val loacation: String, val price: String)
class ProductSearchAdapter(
    private val productSearchs: ArrayList<ProductSearch>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ProductSearchAdapter.ProductSearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_search_item, parent, false)
        return ProductSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductSearchViewHolder, position: Int) {

        val productSearch = productSearchs[position]
        holder.productSearchImage.setImageResource(productSearch.image)
        holder.productSearchName.text = productSearch.name
        holder.productSearchLocation.text = productSearch.loacation
        holder.productSearchPrice.text = productSearch.price
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = productSearchs.size


    class ProductSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productSearchImage: ImageView = itemView.findViewById<ImageView>(R.id.product_search_image)
        val productSearchName: TextView = itemView.findViewById<TextView>(R.id.product_search_name)
        val productSearchLocation: TextView = itemView.findViewById<TextView>(R.id.restuarant_search_name)
        val productSearchPrice: TextView = itemView.findViewById<TextView>(R.id.product_search_price)


    }

}