package com.example.orderfood_app.services

import android.util.Log
import com.example.orderfood_app.models.Slide
import com.example.orderfood_app.models.Notification
import com.google.firebase.database.*

interface SlideCallback {
    fun onSlidesLoaded(slides: ArrayList<Slide>)

}
class SlideService(private val callback: SlideCallback):DAOInterface<Slide> {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("slide")

    override fun create(item: Slide): Notification<Slide> {
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

    override fun getById(id: String): Slide {
        var slide = Slide()
        try {
            databaseReference.child(id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    slide = snapshot.getValue(Slide::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
            return slide
        } catch (e: Exception) {

        }
        return slide
    }

    override fun getAll() {
        var list: ArrayList<Slide> = ArrayList()
        try {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.e("ooooo", "onDataChange: $snapshot")
                    for (data in snapshot.children) {
                        var slide = data.getValue(Slide::class.java)!!
                        list.add(slide)
                    }
                    callback.onSlidesLoaded(list)
                    Log.e("ooooo", "onDataChange: ${list.size}")
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } catch (e: Exception) {

        }
        Log.e("ooooo", "onDataChange: ${list.size}")
    }

    override fun delete(id: String): Notification<Slide> {
        val slide = getById(id)
        if (slide.id != null) {
            databaseReference.child(slide.id.toString()).removeValue()
            return Notification("Success", slide, true)
        }
        return Notification("Failed", slide, false)
    }

    override fun findBy(attribute: String, value: String): ArrayList<Slide> {
        var list: ArrayList<Slide> = ArrayList()
        try {
            databaseReference.orderByChild(attribute).equalTo(value)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            list = ArrayList()
                            for (data in snapshot.children) {
                                list.add(data.getValue(Slide::class.java)!!)
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

    override fun update(id: String, item: Slide): Notification<Slide> {
        val slide = getById(id)
        try {
            if (slide.id != null) {
                databaseReference.child(slide.id.toString()).setValue(item)
                return Notification("Success", item, true)
            }
        } catch (e: Exception) {
            return Notification("Failed", item, false)
        }
        return Notification("Failed", item, false)
    }

}