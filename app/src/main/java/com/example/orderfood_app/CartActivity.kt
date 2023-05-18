package com.example.orderfood_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.adapters.CartAdapter
import com.example.orderfood_app.adapters.OnItemClickListenerCart
import com.example.orderfood_app.models.Cart
import com.example.orderfood_app.models.Notification
import com.example.orderfood_app.models.User
import com.example.orderfood_app.services.CartCallback
import com.example.orderfood_app.services.CartService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.text.NumberFormat

class CartActivity : AppCompatActivity(), OnItemClickListenerCart {

    private var cartAdapter: CartAdapter? = null
    private lateinit var auth: FirebaseAuth


    //    lateinit var cartService: CartService
    var count: Int = 0
    private var carts: ArrayList<Cart> = ArrayList()
    private var selectedId: String? = null
    private var selectedName: String? = null
    private var selectedPrice: String? = null
    private var selectedQuantity: String? = null
    private var selectedImage: String? = null
    private var selectedLocation: String? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceUser: DatabaseReference

    private var selectedCart: Cart? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
//        cartService = CartService(this)
//        cartService.getAll()
        initRecyclerView()
        val idUser = intent.getStringExtra("uid")

        databaseReference = FirebaseDatabase.getInstance().getReference("cart")
        val btn_back = findViewById<ImageView>(R.id.back)
        btn_back.setOnClickListener {
            finish()
            cartAdapter?.notifyDataSetChanged()
        }
        val btn_add = findViewById<ImageView>(R.id.add)
        btn_add.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
            cartAdapter?.notifyDataSetChanged()
        }
        cartAdapter?.setOnClickView {
            selectedId = it.id
            selectedCart = it
        }
        getData()
        val btn_checkout = findViewById<Button>(R.id.checkout_btn)
        btn_checkout.setOnClickListener {
            if (carts.isNotEmpty()) {
                getTotalPrice()
                Log.e("total", getTotalPrice())
                // Thực hiện xử lý thanh toán và kiểm tra các giỏ hàng đã được chọn
                // Tạo Intent hoặc gọi hàm để chuyển đến màn hình thanh toán hoặc xử lý giao dịch
                // Ví dụ:
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("total", getTotalPrice())
                intent.putExtra(
                    "quantity",
                    getTotalQuantity()
                )// Truyền danh sách giỏ hàng cho màn hình thanh toán
                intent.putExtra("idUser", carts.get(0).idUser)
                databaseReferenceUser = FirebaseDatabase.getInstance().getReference("user")
                databaseReferenceUser.child(carts.get(0).idUser)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                // Thực hiện xử lý với thông tin người dùng tại đây
                                Log.d("AccountFragment", "User name: ${user.name}")
                                Log.d("AccountFragment", "User email: ${user.email}")
                                intent.putExtra("name", user.name)
                                intent.putExtra("email", user.email)

                                // ...
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e(
                                "AccountFragment",
                                "Error retrieving user data",
                                error.toException()
                            )
                        }
                    })
                startActivity(intent)
            } else {
                Toast.makeText(this, "Không có mục nào trong giỏ hàng", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun delete() {
        databaseReference?.child(selectedId.orEmpty())?.removeValue()
        cartAdapter?.notifyDataSetChanged()
        reloadActivity()
//        getData()
    }

    fun reloadActivity() {
        recreate()
    }

    private fun initRecyclerView() {
        cartAdapter = CartAdapter()
        val cartRecyclerView = findViewById<RecyclerView>(R.id.cart_recycler_view)
        if (cartRecyclerView != null) {
            cartRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        if (cartRecyclerView != null) {
            cartRecyclerView.adapter = cartAdapter
            cartAdapter?.setOnItemClickListener(this)
            cartAdapter?.setOnClickView {
                selectedId = it.id
                selectedCart = it

            }
            cartAdapter?.notifyDataSetChanged()
        }

    }

    private fun getData() {
        auth = Firebase.auth

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    var cart = data.getValue(Cart::class.java)!!
                    carts.add(cart)
                }
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val uid = currentUser.uid

                    carts = carts.filter { cart ->
                        cart.quantity > "0" && cart.idUser == uid
                    } as ArrayList<Cart>
                }
                cartAdapter?.setItems(carts)
                Log.e("TAG", "onDataChange: ${carts.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "loadPost:onCancelled", error.toException())
            }
        })
    }

    fun create(item: Cart): Notification<Cart> {
        return try {
            val key = databaseReference.push().key
            if (key != null) {
                item.id = key
            }
            databaseReference.child(key!!).setValue(item)
            Notification("Success", item, true)
        } catch (e: Exception) {
            e.printStackTrace()
            Notification("Failed", item, false)
        }
    }

    override fun onDeleteClick() {
        Log.e("Id", selectedId.toString())
        delete()
    }

    override fun onUpdateClick() {
//        val cartQuanlity: TextView = findViewById<TextView>(R.id.quantityEditText)
//
//
//        cartAdapter?.setOnClickView {
//            selectedName = it.name
//            selectedLocation = it.loacation
//            selectedImage = it.image
//            selectedQuantity = it.quantity
//            selectedPrice = it.price
//
//        }
//        var cart = Cart(
//            "${selectedId}",
//            "${selectedName}",
//            "${selectedLocation}",
//            "${selectedPrice}",
//            "${selectedQuantity}",
//            "${selectedImage}",
//        )
//        Log.e("TAG", "onUpdateClick: ${cart.quantity}")
//        update(cart)
//        cartAdapter?.notifyDataSetChanged()

    }

    override fun onClickIncrease() {

//        cartQuanlity.text = (quanlity.toInt() + 1).toString()
        selectedCart?.quantity = (selectedCart?.quantity?.toIntOrNull() ?: 0 + 1).toString()

        // Update cart item in database (assuming update() function handles this)
        selectedCart?.let { update(it) }
//        cartQuanlity.text = (cart.quantity.toInt() + 1).toString()
        Log.e("TAG", ": ${selectedId}")
        cartAdapter?.notifyDataSetChanged()

    }

    override fun onClickDecrease() {

//        cartQuanlity.text = (quanlity.toInt() - 1).toString()

        selectedCart?.quantity = (selectedCart?.quantity?.toIntOrNull() ?: 0 - 1).toString()

        // Update cart item in database (assuming update() function handles this)
        selectedCart?.let { update(it) }

//        cartQuanlity.text = (cart.quantity.toInt() - 1).toString()
        Log.e("TAG", ": ${selectedId}")
        cartAdapter?.notifyDataSetChanged()

    }

    private fun update(item: Cart) {
        try {
            if (selectedId != null) {
                databaseReference.child(selectedId.toString()).setValue(item)
                cartAdapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun formatPriceWithCurrency(price: Int): String {
        val numberFormat: NumberFormat = DecimalFormat("#,###")
        val formattedPrice = numberFormat.format(price)
        return "$formattedPrice VND"
    }

    private fun getTotalPrice(): String {
        var total = 0
        for (cart in carts) {
            val priceString = cart.price.replace(
                "[^\\d]".toRegex(),
                ""
            ) // Loại bỏ ký tự đơn vị (chỉ giữ lại chữ số và dấu chấm)
            Log.e("TAG", "getPriceString: $priceString")
            val price = priceString.toIntOrNull() // Ép kiểu chuỗi thành số nguyên
            if (price != null) {
                total += price * cart.quantity.toIntOrNull()!! ?: 0
            }
            Log.e("TAG", "getTotal: $total")
            Log.e("TAG", "getTotalPrice: $price")
            Log.e("TAG", "getQuantity: ${cart.quantity}")


        }
        val totalValue = formatPriceWithCurrency(total)
        return totalValue
        Log.e("TAG", "getTotalPrice: $totalValue")
    }

    private fun getTotalQuantity(): String {
        var total = 0
        for (cart in carts) {
            val priceString = cart.price.replace(
                "[^\\d]".toRegex(),
                ""
            ) // Loại bỏ ký tự đơn vị (chỉ giữ lại chữ số và dấu chấm)
            Log.e("TAG", "getPriceString: $priceString")
            val price = priceString.toIntOrNull() // Ép kiểu chuỗi thành số nguyên
            if (price != null) {
                total += cart.quantity.toIntOrNull()!! ?: 0
            }
            Log.e("TAG", "getTotal: $total")
            Log.e("TAG", "getTotalPrice: $price")
            Log.e("TAG", "getQuantity: ${cart.quantity}")


        }
        return total.toString()
    }
}