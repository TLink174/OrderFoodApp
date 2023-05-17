package com.example.orderfood_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import coil.load
import com.example.orderfood_app.adapters.CartAdapter
import com.example.orderfood_app.models.Cart
import com.example.orderfood_app.models.Notification
import com.example.orderfood_app.models.Product
import com.example.orderfood_app.services.ProductCallback
import com.google.firebase.database.*

class DetailActivity : AppCompatActivity(), ProductCallback {

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("product")
    var id: String = ""

    private var cartAdapter: CartAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        val detailBack = findViewById<ImageView>(R.id.detail_back)
        val detailAdd = findViewById<Button>(R.id.detail_add_to_cart)
        val detailRating = findViewById<RatingBar>(R.id.detail_rating)
        val detailNavigationView = findViewById<LinearLayout>(R.id.detail_navigation)
        val frameDetail = findViewById<FrameLayout>(R.id.frame_detail)
        val detailImage = findViewById<ImageView>(R.id.detail_image)
        val detailName = findViewById<TextView>(R.id.detail_name)
        val detailPrice = findViewById<TextView>(R.id.detail_price)
        val detailDescription = findViewById<TextView>(R.id.detail_description)

        detailBack.setOnClickListener {
            finish()
            cartAdapter?.notifyDataSetChanged()
        }

        val bundle = intent.extras
        if (bundle != null) {
            var product: Product = bundle.getSerializable("product") as Product
            Log.e("product", product.toString())
            detailImage.load(product.image)
            detailName.text = product.name
            detailPrice.text = product.price
            detailDescription.text = product.description
            detailRating.rating = product.rating.toFloat()
//            databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    product = snapshot.getValue(Product::class.java)!!
//                    Log.e("product", product.id)
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    Log.e("TAG", "loadPost:onCancelled", error.toException())
//                }
//            })
            detailAdd.setOnClickListener {

                val cart = Cart(
                    "",
                    product.name,
                    product.loacation,
                    product.price,
                    "1",
                    product.image,
                    "",
                )
                create(cart)
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                cartAdapter?.notifyDataSetChanged()
            }

        }


    }

    fun create(item: Cart): Notification<Cart> {
        var databaseReferenceCart: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("cart")
        try {
            val key = databaseReferenceCart.push().key
            if (key != null) {
                item.id = key
            }
            databaseReferenceCart.child(key!!).setValue(item)
            Notification("Success", item, true)
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Notification("Failed", item, false)
        }
        return Notification("Success", item, true)
    }

    override fun onProductsLoaded(products: ArrayList<Product>) {

    }

    override fun onProductLoaded(product: Product) {
        val detailNavigationView = findViewById<LinearLayout>(R.id.detail_navigation)
        val frameDetail = findViewById<FrameLayout>(R.id.frame_detail)
        val detailImage = findViewById<ImageView>(R.id.detail_image)
        val detailName = findViewById<TextView>(R.id.detail_name)
        val detailPrice = findViewById<TextView>(R.id.detail_price)
        val detailDescription = findViewById<TextView>(R.id.detail_description)
        val detailBack = findViewById<ImageView>(R.id.detail_back)
        val detailAdd = findViewById<Button>(R.id.detail_add_to_cart)
        val detailRating = findViewById<RatingBar>(R.id.detail_rating)


        detailImage.load(product.image)
        detailName.text = product.name
        detailPrice.text = product.price
        detailDescription.text = product.description
        detailRating.rating = product.rating.toFloat()
        Log.e("product", product.name)
        Log.e("id", product.id)
        id = product.id
    }
}