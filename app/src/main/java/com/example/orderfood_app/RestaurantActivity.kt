package com.example.orderfood_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.adapters.OnItemClickListener
import com.example.orderfood_app.adapters.RestaurantAdapter
import com.example.orderfood_app.models.Restaurant
import com.example.orderfood_app.services.RestaurantCallback
import com.example.orderfood_app.services.RestaurantService

class RestaurantActivity : AppCompatActivity(), RestaurantCallback {

    lateinit var restaurantService: RestaurantService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        restaurantService = RestaurantService(this)
        restaurantService.getAll()

        val btn_back = findViewById<ImageView>(R.id.restaurant_back)
        btn_back.setOnClickListener {
            finish()
        }

    }

    override fun onRestaurantsLoaded(restaurants: ArrayList<Restaurant>) {
        val detailRestaurantRecyclerView =
            findViewById<RecyclerView>(R.id.detail_restaurant_recycler_view)
        if (detailRestaurantRecyclerView != null) {
            detailRestaurantRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        if (detailRestaurantRecyclerView != null) {
            detailRestaurantRecyclerView.adapter = RestaurantAdapter(restaurants, object :
                OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Toast.makeText(
                        this@RestaurantActivity,
                        restaurants[position].name,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intentRestaurant = Intent(this@RestaurantActivity, ProductActivity::class.java)
                    intentRestaurant.putExtra("name", restaurants[position].name)
                    startActivity(intentRestaurant)
                }
            })
        }
    }
}