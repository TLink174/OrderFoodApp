package com.example.orderfood_app.models

data class Restaurant(
    var id: String = "",
    val name: String = "",
    val image: String = "",
    val description: String = "",
    val loacation: String = "",
    val valueLocation: String = "",
    val rating: Float = 0f
)
