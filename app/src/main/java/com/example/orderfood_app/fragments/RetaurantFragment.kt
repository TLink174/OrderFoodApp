package com.example.orderfood_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R
import com.example.orderfood_app.adapters.OnItemClickListener
import com.example.orderfood_app.adapters.RestaurantAdapter
import com.example.orderfood_app.models.Restaurant
import com.example.orderfood_app.services.RestaurantCallback
import com.example.orderfood_app.services.RestaurantService

class RetaurantFragment : Fragment(), OnItemClickListener, RestaurantCallback {

    lateinit var restaurantService: RestaurantService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        restaurantService = RestaurantService(this)
        restaurantService.getAll()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_retaurant, container, false)

        return view
    }

    override fun onItemClick(view: View, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onRestaurantsLoaded(restaurants: ArrayList<Restaurant>) {
        val detailRestaurantRecyclerView =
            view?.findViewById<RecyclerView>(R.id.detail_restaurant_recycler_view)
        if (detailRestaurantRecyclerView != null) {
            detailRestaurantRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        if (detailRestaurantRecyclerView != null) {
            detailRestaurantRecyclerView.adapter = RestaurantAdapter(restaurants, object :
                OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Toast.makeText(context, restaurants[position].name, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}