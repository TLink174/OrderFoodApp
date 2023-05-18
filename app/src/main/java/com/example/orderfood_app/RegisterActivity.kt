package com.example.orderfood_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.orderfood_app.models.Notification
import com.example.orderfood_app.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val btnRegister = findViewById<Button>(R.id.sign_up_btn)

        btnRegister.setOnClickListener {
            SingupUsersdata()
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val uid = currentUser.uid
                val email = currentUser.email

                // Tham chiếu đến nút trong Realtime Database để lưu trữ thông tin người dùng
                databaseReference = FirebaseDatabase.getInstance().getReference("user")


                val user = User(
                    "${uid}",
                    "",
                    "${email}",
                    "",
                    "",
                )
                create(user)

                // Lấy thông tin người dùng từ Realtime Database
                databaseReference.child(uid).get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val userData = dataSnapshot.value as Map<String, Any>

                        // Lấy thông tin avatar từ dữ liệu người dùng (nếu có)
                        val avatarUrl = userData["avatarUrl"] as String?
                        // Hiển thị hình ảnh avatar (nếu có) bằng cách sử dụng thư viện hoặc phương thức tương ứng

                        // Cập nhật thông tin người dùng trong Firebase Realtime Database

                        val updatedUserData = HashMap<String, Any>()
                        updatedUserData["uid"] = uid
                        updatedUserData["email"] = email ?: ""
                        updatedUserData["avatarUrl"] = avatarUrl ?: ""

                        databaseReference.child(uid).setValue(updatedUserData)
                            .addOnSuccessListener {
                                // Thành công
                                Toast.makeText(this, "Dữ liệu đã được lưu trữ thành công.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                // Xảy ra lỗi
                                Toast.makeText(this, "Lưu trữ dữ liệu thất bại.", Toast.LENGTH_SHORT).show()
                            }
                    }else {
                        // Người dùng không tồn tại hoặc chưa xác thực
                        Toast.makeText(this, "Người dùng không tồn tại hoặc chưa xác thực.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val btnLogin = findViewById<TextView>(R.id.sign_in_btn)
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun SingupUsersdata() {
        val email = findViewById<EditText>(R.id.enter_your_email).text.toString()
        val pass = findViewById<EditText>(R.id.password).text.toString()
        val passagain = findViewById<EditText>(R.id.confirm_password).text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            return
        }
        if (pass != passagain) {
            findViewById<EditText>(R.id.confirm_password).error = "Passwords don't match"
            return
        }
        //day du lieu
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val intent = Intent(this, HomeActivity()::class.java)
                startActivity(intent)

                Toast.makeText(baseContext, "Authentication success.", Toast.LENGTH_SHORT,).show()

            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText( baseContext, "Authentication failed.", Toast.LENGTH_SHORT, ).show()
            }
        }.addOnFailureListener {
            Toast.makeText( baseContext, "Authentication failed. ${it.localizedMessage}", Toast.LENGTH_SHORT, ).show()
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
    fun create(item: User): Notification<User> {
        return try {
            val key = databaseReference.push().key
            if (key != null) {
                item.uid = key
            }
            databaseReference.child(key!!).setValue(item)
            Notification("Success", item, true)
        } catch (e: Exception) {
            e.printStackTrace()
            Notification("Failed", item, false)
        }
    }
}