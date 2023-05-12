package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.orderfood_app.R
import com.example.orderfood_app.models.Brand


class BrandAdapter(
    private val brands: ArrayList<Brand>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.brand_item, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {

        val brand = brands[position]
        holder.brandImage.load(brand.image)
        holder.brandName.text = brand.name
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = brands.size


    class BrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandImage: ImageView = itemView.findViewById<ImageView>(R.id.image_brand)
        val brandName: TextView = itemView.findViewById<TextView>(R.id.name_brand)
    }

}