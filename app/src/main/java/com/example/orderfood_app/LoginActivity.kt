package com.example.orderfood_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val btnSignUp = findViewById<TextView>(R.id.sign_up_ek1)
        btnSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val btnSignIn = findViewById<Button>(R.id.sign_in_ek6)
        btnSignIn.setOnClickListener {
            signInUser()
        }
        val btnForgot = findViewById<TextView>(R.id.fogot_password)
        btnForgot.setOnClickListener {
            val intent = Intent(this, ResetpassActivity::class.java)
            startActivity(intent)
        }
        val checkSave = findViewById<CheckBox>(R.id.checkBox)
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val pass = sharedPreferences.getString("password", null)
        val save = sharedPreferences.getBoolean("save", false)
        val enterEmail = findViewById<TextView>(R.id.enter_your_email_ek1)
        val password = findViewById<TextView>(R.id.password_ek1)
        if (save) {
            enterEmail?.text = email
            password?.text = pass
        }
    }

    private fun signInUser() {
        val enterEmail = findViewById<TextView>(R.id.enter_your_email_ek1)?.text.toString()
        val password = findViewById<TextView>(R.id.password_ek1)?.text.toString()

        if (enterEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(enterEmail, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Save login information
                val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("email", enterEmail)
                editor.putString("password", password)
                editor.putBoolean("save", findViewById<CheckBox>(R.id.checkBox)?.isChecked ?: false)
                editor.apply()

                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val exception = task.exception
                val errorMessage = exception?.message
                Toast.makeText(this, "Authentication failed: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
