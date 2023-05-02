package com.example.orderfood_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}