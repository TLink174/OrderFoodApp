package com.example.orderfood_app.services

import android.util.Log
import com.example.orderfood_app.models.Category
import com.example.orderfood_app.models.Notification
import com.google.firebase.database.*


interface CategoryCallback {
    fun onCategoriesLoaded(categories: ArrayList<Category>)

    fun onCategoriesLoadFailed(exception: Exception)
}

class CategoryService(val callback: CategoryCallback) : DAOInterface<Category> {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("category")

    override fun create(item: Category): Notification<Category> {
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

    override fun getById(id: String): Category {
         var category = Category()
        try {
            databaseReference.child(id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    category = snapshot.getValue(Category::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
            return category
        } catch (e: Exception) {

        }
        return category
    }

    override fun getAll() {
        var list: ArrayList<Category> = ArrayList()
        try {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.e("ooooo", "onDataChange: $snapshot")
                    for (data in snapshot.children) {
                        var category = data.getValue(Category::class.java)!!
                        list.add(category)
                    }
                    callback.onCategoriesLoaded(list)
                    Log.e("ooooo", "onDataChange: ${list.size}")
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } catch (e: Exception) {

        }
        Log.e("ooooo", "onDataChange: ${list.size}")
    }

    override fun delete(id: String): Notification<Category> {
        val category = getById(id)
        if (category.id != null) {
            databaseReference.child(category.id.toString()).removeValue()
            return Notification("Success", category, true)
        }
        return Notification("Failed", category, false)
    }

    override fun findBy(attribute: String, value: String): ArrayList<Category> {
        var list: ArrayList<Category> = ArrayList()
        try {
            databaseReference.orderByChild(attribute).equalTo(value)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            list = ArrayList()
                            for (data in snapshot.children) {
                                list.add(data.getValue(Category::class.java)!!)
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

    override fun update(id: String, item: Category): Notification<Category> {
        val category = getById(id)
        try {
            if (category.id != null) {
                databaseReference.child(category.id.toString()).setValue(item)
                return Notification("Success", item, true)
            }
        } catch (e: Exception) {
            return Notification("Failed", item, false)
        }
        return Notification("Failed", item, false)
    }


}