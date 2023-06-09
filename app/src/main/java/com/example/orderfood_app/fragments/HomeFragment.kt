package com.example.orderfood_app.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.DetailActivity
import com.example.orderfood_app.ProductActivity
import com.example.orderfood_app.R
import com.example.orderfood_app.RestaurantActivity
import com.example.orderfood_app.adapters.*
import com.example.orderfood_app.models.*
import com.example.orderfood_app.services.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class HomeFragment : Fragment(), CategoryCallback, SlideCallback, BrandCallback,
    RestaurantCallback, ProductCallback {
    //    private lateinit var categories: ArrayList<Category>

    //    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
//        .getReference("https://orderfoodapp-ltdd-default-rtdb.asia-southeast1.firebasedatabase.app/category")
    lateinit var categoryService: CategoryService
    lateinit var slideService: SlideService
    lateinit var brandService: BrandService
    lateinit var restaurantService: RestaurantService
    lateinit var productService: ProductService
    public var idProduct: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {}

        categoryService = CategoryService(this)
        categoryService.getAll()

        slideService = SlideService(this)
        slideService.getAll()

        brandService = BrandService(this)
        brandService.getAll()

        restaurantService = RestaurantService(this)
        restaurantService.getAll()

        productService = ProductService(this)
        productService.getAll()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        val categoryRecyclerView = view.findViewById<RecyclerView>(R.id.category_recycler_view)
//        categoryRecyclerView.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//        var list = initListCategory()
//        categoryRecyclerView.adapter = CategoryAdapter(list, object : OnItemClickListener {
//            override fun onItemClick(view: View, position: Int) {
//                Toast.makeText(context, list[position].name, Toast.LENGTH_SHORT).show()
//            }
//        })


//        val categories: ArrayList<com.example.orderfood_app.models.Category> =
//            categoryService.getAll()
//        for (i in categories.indices) {
//            Toast.makeText(context, categories[i].name, Toast.LENGTH_SHORT).show()
//        }

        val seeAllRestaurant = view.findViewById<View>(R.id.restaurant_show)
        seeAllRestaurant.setOnClickListener {
            val intent = Intent(activity, RestaurantActivity::class.java)
            startActivity(intent)
        }
        val seeAllRestaurantIcon = view.findViewById<ImageView>(R.id.restaurant_show_icon)
        seeAllRestaurantIcon.setOnClickListener {
            val intent = Intent(activity, RestaurantActivity::class.java)
            startActivity(intent)
        }

        val seeAllProduct = view.findViewById<View>(R.id.product_show)
        seeAllProduct.setOnClickListener {
            val intent = Intent(activity, ProductActivity::class.java)
            startActivity(intent)
        }
        val seeAllProductIcon = view.findViewById<ImageView>(R.id.product_show_icon)
        seeAllProductIcon.setOnClickListener {
            val intent = Intent(activity, ProductActivity::class.java)
            startActivity(intent)
        }
        return view


    }


//    private fun initListCategory(): ArrayList<Category> {
//
//        val category = Category(
//                "", "Home", "https://t4.ftcdn.net/jpg/00/81/38/59/360_F_81385977_wNaDMtgrIj5uU5QEQLcC9UNzkJc57xbu.jpg"
//            )
//        var list : ArrayList<Category> = categoryService.getAll()
//        return list
//    }

//    private fun initListBrand(): ArrayList<Brand> {
//        val list = ArrayList<Brand>()
//        list.add(Brand(1, "Home", R.drawable.okokokok))
//        list.add(Brand(2, "Search", R.drawable.okokokok))
//        list.add(Brand(3, "Order", R.drawable.okokokok))
//        list.add(Brand(4, "Account", R.drawable.okokokok))
//        list.add(Brand(5, "Home", R.drawable.okokokok))
//        return list
//    }

//    private fun initListSlide(): ArrayList<Slide> {
//        val list = ArrayList<Slide>()
//        list.add(Slide(1, "Home", R.drawable.okokokok))
//        list.add(Slide(2, "Search", R.drawable.okokokok))
//        list.add(Slide(3, "Order", R.drawable.okokokok))
//        list.add(Slide(4, "Account", R.drawable.okokokok))
//        list.add(Slide(5, "Home", R.drawable.okokokok))
//        return list
//    }

//    private fun initListProduct(): ArrayList<Product> {
//        val list = ArrayList<Product>()
//        list.add(Product(1, "Home", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        list.add(Product(2, "Search", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        list.add(Product(3, "Order", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        list.add(Product(4, "Account", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        list.add(Product(5, "Home", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        return list
//    }

//    private fun initListRestaurant(): ArrayList<Restaurant> {
//        val list = ArrayList<Restaurant>()
//        list.add(Restaurant(1, "Home", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        list.add(Restaurant(2, "Search", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        list.add(Restaurant(3, "Order", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        list.add(Restaurant(4, "Account", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        list.add(Restaurant(5, "Home", R.drawable.okokokok, "Da Nang", "5,6 km"))
//        return list
//    }

    override fun onCategoriesLoaded(categories: ArrayList<Category>) {
        val categoryRecyclerView = view?.findViewById<RecyclerView>(R.id.category_recycler_view)
        if (categoryRecyclerView != null) {
            categoryRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        if (categoryRecyclerView != null) {
            categoryRecyclerView.adapter =
                CategoryAdapter(categories, object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Toast.makeText(context, categories[position].name, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }

    override fun onCategoriesLoadFailed(exception: Exception) {
        TODO("Not yet implemented")
    }

    override fun onSlidesLoaded(slides: ArrayList<Slide>) {
//        val slide = Slide(
//            "",
//            "Home",
//            "https://pos.nvncdn.net/be3159-662/pc/20230419_vsJf7ty8.png"
//        )
//        Toast.makeText(context, slide.name, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, slideService.create(slide).message, Toast.LENGTH_SHORT).show()
        val slideRecyclerView = view?.findViewById<RecyclerView>(R.id.slide_recycler_view)
        if (slideRecyclerView != null) {
            slideRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        if (slideRecyclerView != null) {
            slideRecyclerView.adapter = SlideAdapter(slides, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Toast.makeText(context, slides[position].name, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onBrandsLoaded(brands: ArrayList<Brand>) {
//        val brand = Brand(
//            "",
//            "McDonald's",
//            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/McDonald%27s_logo.svg/2560px-McDonald%27s_logo.svg.png"
//        )
//        Toast.makeText(context, brand.name, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, brandService.create(brand).message, Toast.LENGTH_SHORT).show()
        val brandRecyclerView = view?.findViewById<RecyclerView>(R.id.brand_recycler_view)
        if (brandRecyclerView != null) {
            brandRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        if (brandRecyclerView != null) {
            brandRecyclerView.adapter = BrandAdapter(brands, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Toast.makeText(context, brands[position].name, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onBrandsLoadFailed(exception: Exception) {
        TODO("Not yet implemented")
    }

    override fun onRestaurantsLoaded(restaurants: ArrayList<Restaurant>) {

//        val restaurant = Restaurant(
//            "",
//            "Ẩm thực Gánh",
//            "https://pos.nvncdn.net/be3159-662/pc/20230419_vsJf7ty8.png",
//            "Ăn vặt",
//            "Đà Nẵng",
//            "10 km",
//            4.5f
//        )
//        Toast.makeText(context, restaurant.name, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, restaurantService.create(restaurant).message, Toast.LENGTH_SHORT)
//            .show()
        val restaurantRecyclerView = view?.findViewById<RecyclerView>(R.id.restaurant_recycler_view)
        if (restaurantRecyclerView != null) {
            restaurantRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        if (restaurantRecyclerView != null) {
            restaurantRecyclerView.adapter =
                RestaurantAdapter(restaurants, object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Toast.makeText(context, restaurants[position].name, Toast.LENGTH_SHORT)
                            .show()
                        val intentRestaurant = Intent(context, ProductActivity::class.java)
                        intentRestaurant.putExtra("name", restaurants[position].name)
                        startActivity(intentRestaurant)
                    }
                })
        }
    }

    override fun onProductsLoaded(products: ArrayList<Product>) {
//        val product = Product(
//            "",
//            "Cá viên chiên",
//            "https://cdn.tgdd.vn/Files/2021/02/02/1324928/5-quan-ca-vien-chien-tai-sai-gon-ma-tin-do-an-vat-nao-cung-biet-202201110147493182.jpg",
//            "Ăn vặt",
//            "20.000 VND",
//            "Sà Bì Chưởng",
//            4f
//        )
//        Toast.makeText(context, product.name, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, productService.create(product).message, Toast.LENGTH_SHORT).show()
        val productRecyclerView = view?.findViewById<RecyclerView>(R.id.product_recycler_view)
        if (productRecyclerView != null) {
            productRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        if (productRecyclerView != null) {
            productRecyclerView.adapter = ProductAdapter(products, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Toast.makeText(context, products[position].name, Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("product", products[position])
                    startActivity(intent)
                }
            })
        }
    }

    override fun onProductLoaded(product: Product) {
        TODO("Not yet implemented")
    }

}