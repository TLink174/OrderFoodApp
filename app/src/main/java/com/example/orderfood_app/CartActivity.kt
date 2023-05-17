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
import com.example.orderfood_app.services.CartCallback
import com.example.orderfood_app.services.CartService
import com.google.firebase.database.*

class CartActivity : AppCompatActivity(), OnItemClickListenerCart {

    private var cartAdapter: CartAdapter? = null

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
    private var selectedCart: Cart? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

//        cartService = CartService(this)
//        cartService.getAll()
        initRecyclerView()
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
        val btn_checkout = findViewById<Button>(R.id.checkout_btn)
        btn_checkout.setOnClickListener {
            getData()
            if (carts.isNotEmpty()) {
                // Thực hiện xử lý thanh toán và kiểm tra các giỏ hàng đã được chọn
                // Tạo Intent hoặc gọi hàm để chuyển đến màn hình thanh toán hoặc xử lý giao dịch
                // Ví dụ:
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("carts", carts) // Truyền danh sách giỏ hàng cho màn hình thanh toán
                startActivity(intent)
            } else {
                Toast.makeText(this, "Không có mục nào trong giỏ hàng", Toast.LENGTH_SHORT).show()
            }
        }
        getData()
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

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    var cart = data.getValue(Cart::class.java)!!
                    carts.add(cart)
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


//    override fun onCartsLoaded(carts: ArrayList<Cart>) {
//        val cart = Cart(
//            "",
//            "Cá viên chiên",
//            "",
//            "20.000 VND",
//            "2",
//            "https://cdn.tgdd.vn/Files/2021/02/02/1324928/5-quan-ca-vien-chien-tai-sai-gon-ma-tin-do-an-vat-nao-cung-biet-202201110147493182.jpg",
//            "",
//        )
//        Toast.makeText(this, cart.name, Toast.LENGTH_SHORT).show()
//        Toast.makeText(this, cartService.create(cart).message, Toast.LENGTH_SHORT).show()
//        val cartRecyclerView = findViewById<RecyclerView>(R.id.cart_recycler_view)
//        if (cartRecyclerView != null) {
//            cartRecyclerView.layoutManager =
//                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        }
//        Log.e("TAG", "onCreate: ${carts}")
////        cartsRestaurant =
////            carts.filter { it.loacation.startsWith(location) } as ArrayList<Product>
////        Log.e("TAG", "onCreate: ${productsRestaurant}")
//        if (cartRecyclerView != null) {
//            cartRecyclerView.adapter =
//                CartAdapter(carts, object : OnItemClickListenerCart {
//                    override fun onItemClick(view: View, position: Int) {
//                        Toast.makeText(
//                            this@CartActivity,
//                            carts[position].name,
//                            Toast.LENGTH_SHORT
//                        ).show()
////                        val intent = Intent(this@ProductActivity, DetailActivity::class.java)
////                        intent.putExtra("cart", carts[position])
////                        startActivity(intent)
//                    }
//
//                    override fun onIncreaseClick(view: View, position: Int) {
//                        // Xử lý sự kiện tăng số lượng mục tại vị trí `position` trong giỏ hàng
//                        val cartItem = carts[position]
//                        count = cartItem.quantity.toInt()
//                        count++
//                        cartItem.quantity = count.toString()
//                    }
//
//                    override fun onDecreaseClick(view: View, position: Int) {
//                        // Xử lý sự kiện giảm số lượng mục tại vị trí `position` trong giỏ hàng
//                        val cartItem = carts[position]
//                        count = cartItem.quantity.toInt()
//
//                        if (count > 0) {
//                            count--
//                            cartItem.quantity = count.toString()
//                        }
//                    }
//
//                    override fun onDeleteClick(view: View, position: Int) {
//                        // Xử lý sự kiện xóa mục tại vị trí `position` khỏi giỏ hàng
//                        Log.e("TAG", "onDeleteClick: ${carts[position].id}")
//                        cartService.delete(carts[position].id)
//                    }
//                })
//        }
//    }
//
//    override fun onCartLoaded(cart: Cart?) {
//        TODO("Not yet implemented")
//    }
}