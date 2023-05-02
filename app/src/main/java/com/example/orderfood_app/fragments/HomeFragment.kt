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
import com.example.orderfood_app.adapters.*

class HomeFragment : Fragment() {
//    private lateinit var categories: ArrayList<Category>
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {}

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val categoryRecyclerView = view.findViewById<RecyclerView>(R.id.category_recycler_view)
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val list = initListCategory()
        categoryRecyclerView.adapter = CategoryAdapter(list, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, list[position].name, Toast.LENGTH_SHORT).show()
            }
        })

        val brandRecyclerView = view.findViewById<RecyclerView>(R.id.brand_recycler_view)
        brandRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val initListBrand = initListBrand()
        brandRecyclerView.adapter = BrandAdapter(initListBrand, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, initListBrand[position].name, Toast.LENGTH_SHORT).show()
            }
        })

        val slideRecyclerView = view.findViewById<RecyclerView>(R.id.slide_recycler_view)
        slideRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val initListSlide = initListSlide()
        slideRecyclerView.adapter = SlideAdapter(initListSlide, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, initListSlide[position].name, Toast.LENGTH_SHORT).show()
            }
        })

        val productRecyclerView = view.findViewById<RecyclerView>(R.id.product_recycler_view)
        productRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val initListProduct = initListProduct()
        productRecyclerView.adapter = ProductAdapter(initListProduct, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, initListProduct[position].name, Toast.LENGTH_SHORT).show()
            }
        })

        val restaurantRecyclerView = view.findViewById<RecyclerView>(R.id.restaurant_recycler_view)
        restaurantRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val initListRestaurant = initListRestaurant()
        restaurantRecyclerView.adapter =
            RestaurantAdapter(initListRestaurant, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Toast.makeText(context, initListRestaurant[position].name, Toast.LENGTH_SHORT)
                        .show()
                }
            })



        return view
    }

    private fun initListCategory(): ArrayList<Category> {
        val list = ArrayList<Category>()
        list.add(Category(1, "Home", R.drawable.okokokok))
        list.add(Category(2, "Search", R.drawable.okokokok))
        list.add(Category(3, "Order", R.drawable.okokokok))
        list.add(Category(4, "Account", R.drawable.okokokok))
        list.add(Category(5, "Home", R.drawable.okokokok))
        return list

    }

    private fun initListBrand(): ArrayList<Brand> {
        val list = ArrayList<Brand>()
        list.add(Brand(1, "Home", R.drawable.okokokok))
        list.add(Brand(2, "Search", R.drawable.okokokok))
        list.add(Brand(3, "Order", R.drawable.okokokok))
        list.add(Brand(4, "Account", R.drawable.okokokok))
        list.add(Brand(5, "Home", R.drawable.okokokok))
        return list
    }

    private fun initListSlide(): ArrayList<Slide> {
        val list = ArrayList<Slide>()
        list.add(Slide(1, "Home", R.drawable.okokokok))
        list.add(Slide(2, "Search", R.drawable.okokokok))
        list.add(Slide(3, "Order", R.drawable.okokokok))
        list.add(Slide(4, "Account", R.drawable.okokokok))
        list.add(Slide(5, "Home", R.drawable.okokokok))
        return list
    }

    private fun initListProduct(): ArrayList<Product> {
        val list = ArrayList<Product>()
        list.add(Product(1, "Home", R.drawable.okokokok, "Da Nang", "5,6 km"))
        list.add(Product(2, "Search", R.drawable.okokokok, "Da Nang", "5,6 km"))
        list.add(Product(3, "Order", R.drawable.okokokok, "Da Nang", "5,6 km"))
        list.add(Product(4, "Account", R.drawable.okokokok, "Da Nang", "5,6 km"))
        list.add(Product(5, "Home", R.drawable.okokokok, "Da Nang", "5,6 km"))
        return list
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