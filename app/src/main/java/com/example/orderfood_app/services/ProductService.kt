package com.example.orderfood_app.services

import android.util.Log
import com.example.orderfood_app.models.Notification
import com.example.orderfood_app.models.Product
import com.google.firebase.database.*

interface ProductCallback {
    fun onProductsLoaded(products: ArrayList<Product>)

    fun onProductLoaded(product: Product)

}

class ProductService (val callback: ProductCallback) : DAOInterface<Product> {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("product")

    override fun create(item: Product): Notification<Product> {
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

    override fun getById(id: String): Product {
        var product = Product()
        try {
            databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    product = snapshot.getValue(Product::class.java)!!
                    callback.onProductLoaded(product)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
            return product
        } catch (e: Exception) {

        }

        return product
    }

    override fun getAll() {
        var list: ArrayList<Product> = ArrayList()
        try {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        var product = data.getValue(Product::class.java)!!
                        list.add(product)
                    }
                    callback.onProductsLoaded(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
        } catch (e: Exception) {

        }
        Log.e("ooooo", "onDataChange: ${list.size}")
    }

    override fun delete(id: String): Notification<Product> {
        val product = getById(id)
        if (product.id != null) {
            databaseReference.child(product.id.toString()).removeValue()
            return Notification("Success", product, true)
        }
        return Notification("Failed", product, false)
    }

    override fun findBy(attribute: String, value: String): ArrayList<Product> {
        var list: ArrayList<Product> = ArrayList()
        try {
            databaseReference.orderByChild(attribute).equalTo(value)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            list = ArrayList()
                            for (data in snapshot.children) {
                                list.add(data.getValue(Product::class.java)!!)
                            }
                        }
                        callback.onProductsLoaded(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        } catch (e: Exception) {

        }
        return list
    }

    override fun update(id: String, item: Product): Notification<Product> {
        val product = getById(id)
        try {
            if (product.id != null) {
                databaseReference.child(product.id.toString()).setValue(item)
                return Notification("Success", item, true)
            }
        } catch (e: Exception) {
            return Notification("Failed", item, false)
        }
        return Notification("Failed", item, false)
    }

}