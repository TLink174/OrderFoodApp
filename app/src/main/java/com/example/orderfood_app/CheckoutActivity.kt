package com.example.orderfood_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.adapters.CartAdapter
import com.example.orderfood_app.fragments.OrderFragment
import com.example.orderfood_app.models.Cart
import com.example.orderfood_app.models.Notification
import com.example.orderfood_app.models.Order
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    private var cartAdapter: CartAdapter? = null

    private var firebaseDatabase: FirebaseDatabase? = null
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        initRecyclerView()
        val totalValue  = intent.getStringExtra("total")
        val totalQuantity = intent.getStringExtra("quantity")
        val total_price = findViewById<TextView>(R.id.total_price)
        total_price.text = totalValue
        val order_checkout = findViewById<Button>(R.id.order_checkout)
        val editName = findViewById<TextView>(R.id.editName)
        val editAddress = findViewById<TextView>(R.id.editAddress)
        val editPhone = findViewById<TextView>(R.id.editTextPhone)
        val editEmail = findViewById<TextView>(R.id.editEmail)
        val date : String = getCurrentDate()
        order_checkout.setOnClickListener {
            var order = Order(
                "",
                "Đã nhận",
                "${editAddress.text}",
                "${editName.text}",
                "${editPhone.text}",
                "${editEmail.text}",
                "${totalValue}",
                "${totalQuantity}",
                "${date}"
            )
            create(order)
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragment_home, OrderFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
        }

        val btn_back = findViewById<ImageView>(R.id.back_checkout)
        btn_back.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView() {
        cartAdapter = CartAdapter()
        val cartRecyclerView = findViewById<RecyclerView>(R.id.cart_recycler_view)
        if (cartRecyclerView != null) {
            cartRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

    }

    fun create(item: Order): Notification<Order> {
        databaseReference = FirebaseDatabase.getInstance().getReference("order")
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

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // Tháng bắt đầu từ 0, nên cộng thêm 1
        val year = calendar.get(Calendar.YEAR)

        // Tạo chuỗi ngày/tháng/năm theo định dạng mong muốn, ví dụ: 17/05/2023
        val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month, year)
        return formattedDate
    }
}