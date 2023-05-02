package com.example.orderfood_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.orderfood_app.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), OnThemeChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val frameContainer = findViewById<FrameLayout>(R.id.frame_container)
        setFragment(HomeFragment())
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setFragment(HomeFragment())
                R.id.search -> setFragment(SearchFragment())
                R.id.order -> setFragment(OrderFragment())
                R.id.account -> setFragment(AccountFragment())
            }
            true
        }



    }

    override fun onThemeChanged() {
        recreate()
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }


}