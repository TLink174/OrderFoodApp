package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R

interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)
}

data class Category(val id: Int, val name: String, val image: Int)
class CategoryAdapter(private val categories: ArrayList<Category>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val category = categories[position]
        holder.categoryName.text = category.name
        holder.categoryImage.setImageResource(category.image)
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = categories.size


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById<ImageView>(R.id.image_category)
        val categoryName: TextView = itemView.findViewById<TextView>(R.id.name_category)


    }

}