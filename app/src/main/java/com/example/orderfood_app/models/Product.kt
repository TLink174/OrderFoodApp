package com.example.orderfood_app.models

data class Product (
    var id: String = "",
    var name: String = "",
    var image: String = "",
    var description: String = "",
    var price: String = "",
    var loacation: String = "",
    var rating: Float = 0f
)