package com.example.orderfood_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailNavigationView = findViewById<LinearLayout>(R.id.detail_navigation)
        val frameDetail = findViewById<FrameLayout>(R.id.frame_detail)
        val detailBack = findViewById<ImageView>(R.id.detail_back)
        detailBack.setOnClickListener {
            finish()
        }
    }
}