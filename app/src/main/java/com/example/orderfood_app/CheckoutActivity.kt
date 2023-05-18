package com.example.orderfood_app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
        val idUser = intent.getStringExtra("idUser")
        val getEmail = intent.getStringExtra("email")
        val getName = intent.getStringExtra("name")
        val total_price = findViewById<TextView>(R.id.total_price)

        val order_checkout = findViewById<Button>(R.id.order_checkout)
        val editName = findViewById<EditText>(R.id.editName)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val editPhone = findViewById<EditText>(R.id.editTextPhone)
        val editEmail = findViewById<EditText>(R.id.editEmail)




        val date : String = getCurrentDate()
        total_price.text = totalValue

        val name = editName.text
        val address = editAddress.text
        val phone = editPhone.text
        val email = editEmail.text
        order_checkout.setOnClickListener {
            Log.d("checkout", "name: $name, address: $address, phone: $phone, email: $email")
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            } else if(!isPhoneNumberValid(phone.toString())){
                Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show()
            } else {
                var order = Order(
                    "",
                    "Đã nhận",
                    "${editAddress.text}",
                    "${editName.text}",
                    "${editPhone.text}",
                    "${editEmail.text}",
                    "${totalValue}",
                    "${totalQuantity}",
                    "${date}",
                    "${idUser}"
                )
                create(order)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
            }

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

    fun formatPhoneNumber(phoneNumber: String): String {
        val formattedPhoneNumber = StringBuilder()

        // Remove all non-digit characters from the phone number
        val digitsOnly = phoneNumber.replace("\\D+".toRegex(), "")

        // Apply formatting based on the number of digits
        if (digitsOnly.length >= 10) {
            // Format as: (XXX) XXX-XXXX
            formattedPhoneNumber.append("(")
                .append(digitsOnly.substring(0, 3))
                .append(") ")
                .append(digitsOnly.substring(3, 6))
                .append("-")
                .append(digitsOnly.substring(6, 10))
        } else {
            // Return the original input if it doesn't have enough digits for formatting
            return phoneNumber
        }

        return formattedPhoneNumber.toString()
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val phonePattern = "^(\\+?84|0)(3[2-9]|5[2689]|7[06789]|8[1-689]|9[0-9])[0-9]{7}\$".toRegex()
        return phonePattern.matches(phoneNumber)
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