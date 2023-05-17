@file:Suppress("SENSELESS_COMPARISON")

package com.example.orderfood_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.adapters.CartAdapter
import com.example.orderfood_app.adapters.OnItemClickListener
import com.example.orderfood_app.adapters.ProductAdapter
import com.example.orderfood_app.models.Product
import com.example.orderfood_app.services.ProductCallback
import com.example.orderfood_app.services.ProductService

class ProductActivity : AppCompatActivity(), ProductCallback {

    lateinit var productService: ProductService
    var productsRestaurant: ArrayList<Product> = ArrayList()
    var location: String = ""
    val attribute: String = "location"
    private var cartAdapter: CartAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val bundle = intent.extras
        if (bundle != null) {
            location = bundle.getString("name").toString()
            Log.e("TAG", "Name: $location")
        }
        productService = ProductService(this)
        productService.getAll()

        val btn_back = findViewById<ImageView>(R.id.product_back)
        btn_back.setOnClickListener {
            location = ""
            finish()

        }
    }

    override fun onProductsLoaded(products: ArrayList<Product>) {
        val productRecyclerView = findViewById<RecyclerView>(R.id.product_recycler_view)
        if (productRecyclerView != null) {
            productRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        if (location != "") {
            productsRestaurant =
                products.filter { it.loacation.startsWith(location) } as ArrayList<Product>
            Log.e("TAG", "onCreate: ${productsRestaurant}")
            if (productRecyclerView != null) {
                productRecyclerView.adapter =
                    ProductAdapter(productsRestaurant, object : OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            Toast.makeText(
                                this@ProductActivity,
                                productsRestaurant[position].name,
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@ProductActivity, DetailActivity::class.java)
                            intent.putExtra("product", products[position])
                            startActivity(intent)
                        }
                    })
            }
        }

        if (location == "") {
            if (productRecyclerView != null) {
                productRecyclerView.adapter =
                    ProductAdapter(products, object : OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            Toast.makeText(
                                this@ProductActivity,
                                products[position].name,
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@ProductActivity, DetailActivity::class.java)
                            intent.putExtra("product", products[position])
                            startActivity(intent)
                        }
                    })
            }
        }

    }

    override fun onProductLoaded(product: Product) {
        TODO("Not yet implemented")
    }
}