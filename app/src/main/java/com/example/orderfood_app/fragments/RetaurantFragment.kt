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
import com.example.orderfood_app.adapters.Restaurant
import com.example.orderfood_app.adapters.RestaurantAdapter

class RetaurantFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_retaurant, container, false)

        val detailRestaurantRecyclerView = view.findViewById<RecyclerView>(R.id.detail_restaurant_recycler_view)
        detailRestaurantRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val initListRestaurant = initListRestaurant()
        detailRestaurantRecyclerView.adapter = RestaurantAdapter(initListRestaurant, object :
            OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, initListRestaurant[position].name, Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

    private fun initListRestaurant(): ArrayList<Restaurant> {
        val list = ArrayList<Restaurant>()
        list.add(Restaurant(1, "Home", R.drawable.okokokok, "Da Nang", "5,6 km"))
        list.add(Restaurant(2, "Search", R.drawable.okokokok, "Da Nang", "5,6 km"))
        list.add(Restaurant(3, "Order", R.drawable.okokokok, "Da Nang", "5,6 km"))
        list.add(Restaurant(4, "Account", R.drawable.okokokok, "Da Nang", "5,6 km"))
        list.add(Restaurant(5, "Home", R.drawable.okokokok, "Da Nang", "5,6 km"))
        return list
    }
}