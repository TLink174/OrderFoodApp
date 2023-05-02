package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R


data class Restaurant(val id: Int, val name: String, val image: Int, val loacation: String, val valueLocation: String)
class RestaurantAdapter(
    private val restaurants: ArrayList<Restaurant>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {

        val restaurant = restaurants[position]
        holder.restaurantImage.setImageResource(restaurant.image)
        holder.restaurantName.text = restaurant.name
        holder.restaurantLocation.text = restaurant.loacation
        holder.restaurantValueLocation.text = restaurant.valueLocation
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = restaurants.size


    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantImage: ImageView = itemView.findViewById<ImageView>(R.id.restaurant_image)
        val restaurantName: TextView = itemView.findViewById<TextView>(R.id.restaurant_name)
        val restaurantLocation: TextView = itemView.findViewById<TextView>(R.id.location_restaurant_lable)
        val restaurantValueLocation: TextView = itemView.findViewById<TextView>(R.id.value_location)


    }

}