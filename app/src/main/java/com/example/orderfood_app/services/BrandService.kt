package com.example.orderfood_app.services

import android.util.Log
import com.example.orderfood_app.models.Brand
import com.example.orderfood_app.models.Notification
import com.google.firebase.database.*


interface BrandCallback {
    fun onBrandsLoaded(brands: ArrayList<Brand>)

    fun onBrandsLoadFailed(exception: Exception)
}

class BrandService(val callback: BrandCallback) : DAOInterface<Brand> {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("brand")

    override fun create(item: Brand): Notification<Brand> {
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

    override fun getById(id: String): Brand {
        var brand = Brand()
        try {
            databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    brand = snapshot.getValue(Brand::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
            return brand
        } catch (e: Exception) {

        }
        return brand
    }


    override fun getAll() {
        var list: ArrayList<Brand> = ArrayList()
        try {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.e("ooooo", "onDataChange: $snapshot")
                    for (data in snapshot.children) {
                        var brand = data.getValue(Brand::class.java)!!
                        list.add(brand)
                    }
                    callback.onBrandsLoaded(list)
                    Log.e("ooooo", "onDataChange: ${list.size}")
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } catch (e: Exception) {

        }
        Log.e("ooooo", "onDataChange: ${list.size}")
    }

    override fun delete(id: String): Notification<Brand> {
        val brand = getById(id)
        if (brand.id != null) {
            databaseReference.child(brand.id.toString()).removeValue()
            return Notification("Success", brand, true)
        }
        return Notification("Failed", brand, false)
    }

    override fun findBy(attribute: String, value: String): ArrayList<Brand> {
        var list: ArrayList<Brand> = ArrayList()
        try {
            databaseReference.orderByChild(attribute).equalTo(value)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            list = ArrayList()
                            for (data in snapshot.children) {
                                list.add(data.getValue(Brand::class.java)!!)
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

    override fun update(id: String, item: Brand): Notification<Brand> {
        val brand = getById(id)
        try {
            if (brand.id != null) {
                databaseReference.child(brand.id.toString()).setValue(item)
                return Notification("Success", item, true)
            }
        } catch (e: Exception) {
            return Notification("Failed", item, false)
        }
        return Notification("Failed", item, false)
    }


}