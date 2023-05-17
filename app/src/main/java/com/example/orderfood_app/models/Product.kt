package com.example.orderfood_app.models

import java.io.Serializable

data class Product(
    var id: String = "",
    var name: String = "",
    var image: String = "",
    var description: String = "",
    var category: String = "",
    var price: String = "",
    var loacation: String = "",
    var rating: Float = 0f


) : Serializable {
    override fun toString(): String {
        return "Product(id='$id', name='$name', image='$image', description='$description', price='$price', loacation='$loacation', rating=$rating)"
    }
    fun getvalue(): String {
        return "$name - $price - $loacation - $category"
    }
}