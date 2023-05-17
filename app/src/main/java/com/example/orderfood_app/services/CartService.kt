package com.example.orderfood_app.services

import android.util.Log
import com.example.orderfood_app.models.Notification
import com.example.orderfood_app.models.Cart
import com.google.firebase.database.*

interface CartCallback {
    fun onCartsLoaded(carts: ArrayList<Cart>)

    fun onCartLoaded(cart: Cart?)

}


class CartService (val callback: CartCallback) : DAOInterface<Cart> {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("cart")

    override fun create(item: Cart): Notification<Cart> {
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

    override fun getById(id: String): Cart {
        var cart = Cart()
        try {
            databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val cart = snapshot.getValue(Cart::class.java)
                    if (cart != null) {
                        callback.onCartLoaded(cart)
                    } else {
                        // Xử lý trường hợp không tìm thấy giỏ hàng với id đã cho
                        // Nếu cần, bạn có thể gọi callback.onCartLoaded(null) để thông báo rằng không có giỏ hàng nào được tìm thấy
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
        } catch (e: Exception) {
            // Xử lý ngoại lệ nếu có
        }
        return cart
    }

    override fun getAll() {
        var list: ArrayList<Cart> = ArrayList()
        try {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        var cart = data.getValue(Cart::class.java)!!
                        list.add(cart)
                    }
                    callback.onCartsLoaded(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
        } catch (e: Exception) {

        }
        Log.e("ooooo", "onDataChange: ${list.size}")
    }

    override fun delete(id: String): Notification<Cart> {
        var list: ArrayList<Cart> = ArrayList()
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    var cart = data.getValue(Cart::class.java)!!
                    list.add(cart)
                }
                callback.onCartsLoaded(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "loadPost:onCancelled", error.toException())
            }
        })
        Log.e("ooooo", "onDataChange: ${list.size}")
        val cart : Cart = list.filter() { it.id == id }.first()
        Log.e("ooooo", "id: ${cart.id}")
        if (cart.id != null) {
            databaseReference.child(cart.id).removeValue()
            return Notification("Success", cart, true)
        }
        return Notification("Failed", cart, false)
    }

    override fun findBy(attribute: String, value: String): ArrayList<Cart> {
        var list: ArrayList<Cart> = ArrayList()
        try {
            databaseReference.orderByChild(attribute).equalTo(value)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            list = ArrayList()
                            for (data in snapshot.children) {
                                list.add(data.getValue(Cart::class.java)!!)
                            }
                        }
                        callback.onCartsLoaded(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        } catch (e: Exception) {

        }
        return list
    }

    override fun update(id: String, item: Cart): Notification<Cart> {
        val cart = getById(id)
        try {
            if (cart.id != null) {
                databaseReference.child(cart.id.toString()).setValue(item)
                return Notification("Success", item, true)
            }
        } catch (e: Exception) {
            return Notification("Failed", item, false)
        }
        return Notification("Failed", item, false)
    }

}