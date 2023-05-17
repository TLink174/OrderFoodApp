package com.example.orderfood_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.DetailActivity
import com.example.orderfood_app.ProductActivity
import com.example.orderfood_app.R
import com.example.orderfood_app.adapters.*
import com.example.orderfood_app.models.Category
import com.example.orderfood_app.models.Product
import com.example.orderfood_app.services.CategoryCallback
import com.example.orderfood_app.services.CategoryService
import com.example.orderfood_app.services.ProductCallback
import com.example.orderfood_app.services.ProductService
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment(), ProductCallback, CategoryCallback {
    lateinit var categoryService: CategoryService
    lateinit var productService: ProductService
    var searchQuery: SearchView? = null
    var searchKeyword = ""
    var list = ArrayList<Product>()
    var initListProductSearch = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productService = ProductService(this)
        productService.getAll()

        categoryService = CategoryService(this)
        categoryService.getAll()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchQuery = view.findViewById<SearchView>(R.id.search_text)
        searchQuery?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Xử lý khi người dùng submit từ khóa tìm kiếm
                searchKeyword = query ?: ""
                Log.e("searchKeyword", searchKeyword)
                searchProductsAndUpdateRecyclerView(list, searchKeyword)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Lấy giá trị từ SearchView khi người dùng thay đổi từ khóa tìm kiếm
                searchKeyword = newText.trim()
                Log.e("searchKeyword", searchKeyword)
                // Xử lý tìm kiếm với từ khóa searchKeyword
                searchProductsAndUpdateRecyclerView(list, searchKeyword)
                return true
            }
        })


        return view
    }

    override fun onProductsLoaded(products: ArrayList<Product>) {
        list = products
        Log.e("initListProductSearch", list.toString())
        val productSearchRecyclerView =
            view?.findViewById<RecyclerView>(R.id.product_search_recycler_view)
        if (productSearchRecyclerView != null) {
            productSearchRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onProductLoaded(product: Product) {
        TODO("Not yet implemented")
    }

    private fun searchProductsAndUpdateRecyclerView(
        products: ArrayList<Product>,
        searchKeyword: String
    ) {
        val filteredProducts = searchProducts(products, searchKeyword)
        Log.e("list", filteredProducts.toString())
        val productSearchRecyclerView =
            view?.findViewById<RecyclerView>(R.id.product_search_recycler_view)
        if (productSearchRecyclerView != null) {
            productSearchRecyclerView.adapter =
                ProductSearchAdapter(filteredProducts, object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Toast.makeText(
                            context,
                            filteredProducts[position].name,
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("product", products[position])
                        startActivity(intent)
                    }
                })
        }
    }

    fun searchProducts(products: ArrayList<Product>, searchKeyword: String): ArrayList<Product> {
        val filteredProducts = ArrayList<Product>()
        for (product in products) {
            if (product.getvalue().lowercase(Locale.ROOT).contains(
                    searchKeyword.lowercase(
                        Locale.ROOT
                    )
                )
            ) {
                filteredProducts.add(product)
            }
        }
        if (filteredProducts.isEmpty()) {
            Toast.makeText(context, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show()
        }
        return filteredProducts
    }
    fun searchCategory(categories: ArrayList<Category>, searchKeyword: String): ArrayList<Category> {
        val filteredCategory = ArrayList<Category>()
        for (category in categories) {
            if (category.toString().lowercase(Locale.ROOT).contains(
                    searchKeyword.lowercase(
                        Locale.ROOT
                    )
                )
            ) {
                filteredCategory.add(category)
            }
        }
        if (filteredCategory.isEmpty()) {
            Toast.makeText(context, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show()
        }
        return filteredCategory
    }

    override fun onCategoriesLoaded(categories: ArrayList<Category>) {
        val suggestRecyclerView = view?.findViewById<RecyclerView>(R.id.suggest_recycler_view)
        if (suggestRecyclerView != null) {
            suggestRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        if (suggestRecyclerView != null) {
            suggestRecyclerView.adapter = SuggestAdapter(categories, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Toast.makeText(context, categories[position].name, Toast.LENGTH_SHORT).show()
                    searchKeyword = categories[position].name
                    Log.e("searchKeyword", searchKeyword)
                    searchProductsAndUpdateRecyclerView(list, searchKeyword)
                }
            })
        }
    }

    override fun onCategoriesLoadFailed(exception: Exception) {
        TODO("Not yet implemented")
    }
}