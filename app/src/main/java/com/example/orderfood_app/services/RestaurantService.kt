package com.example.orderfood_app.services

import android.util.Log
import com.example.orderfood_app.models.Notification
import com.example.orderfood_app.models.Restaurant
import com.google.firebase.database.*

interface RestaurantCallback {
    fun onRestaurantsLoaded(restaurants: ArrayList<Restaurant>)
}

class RestaurantService (val callback: RestaurantCallback) :DAOInterface<Restaurant> {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("restaurant")

    override fun create(item: Restaurant): Notification<Restaurant> {
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

    override fun getById(id: String): Restaurant {
        var restaurant = Restaurant()
        try {
            databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    restaurant = snapshot.getValue(Restaurant::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
            return restaurant
        } catch (e: Exception) {

        }

        return restaurant
    }

    override fun getAll() {
        var list: ArrayList<Restaurant> = ArrayList()
        try {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        var restaurant = data.getValue(Restaurant::class.java)!!
                        list.add(restaurant)
                    }
                    callback.onRestaurantsLoaded(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
        } catch (e: Exception) {

        }
        Log.e("ooooo", "onDataChange: ${list.size}")
    }

    override fun delete(id: String): Notification<Restaurant> {
        val restaurant = getById(id)
        if (restaurant.id != null) {
            databaseReference.child(restaurant.id.toString()).removeValue()
            return Notification("Success", restaurant, true)
        }
        return Notification("Failed", restaurant, false)
    }

    override fun findBy(attribute: String, value: String): ArrayList<Restaurant> {
        var list: ArrayList<Restaurant> = ArrayList()
        try {
            databaseReference.orderByChild(attribute).equalTo(value)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            list = ArrayList()
                            for (data in snapshot.children) {
                                list.add(data.getValue(Restaurant::class.java)!!)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        } catch (e: Exception) {

        }
        return list
    }

    override fun update(id: String, item: Restaurant): Notification<Restaurant> {
        val restaurant = getById(id)
        try {
            if (restaurant.id != null) {
                databaseReference.child(restaurant.id.toString()).setValue(item)
                return Notification("Success", item, true)
            }
        } catch (e: Exception) {
            return Notification("Failed", item, false)
        }
        return Notification("Failed", item, false)
    }


}
