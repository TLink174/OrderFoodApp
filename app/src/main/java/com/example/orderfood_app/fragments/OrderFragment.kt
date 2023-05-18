package com.example.orderfood_app.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.CartActivity
import com.example.orderfood_app.ProductActivity
import com.example.orderfood_app.R
import com.example.orderfood_app.adapters.*
import com.example.orderfood_app.models.Cart
import com.example.orderfood_app.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class OrderFragment : Fragment(), OnItemClickListenerOrder {

    private lateinit var orderAdapter: OrderAdapter
    private var orders: ArrayList<Order> = ArrayList()
    private var firebaseDatabase: FirebaseDatabase? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        databaseReference = FirebaseDatabase.getInstance().getReference("order")
        orderAdapter = OrderAdapter()

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        val orderRecyclerView = view.findViewById<RecyclerView>(R.id.order_recycler_view)
        orderRecyclerView.adapter = orderAdapter


        val orderBtn = view.findViewById<Button>(R.id.order_btn)
        val completeIcon = view.findViewById<ImageView>(R.id.complete_icon)
        val completeText = view.findViewById<TextView>(R.id.complete_text)
        val canceledIcon = view.findViewById<ImageView>(R.id.canceled_icon)
        val canceledText = view.findViewById<TextView>(R.id.canceled_text)
        val processingIcon = view.findViewById<ImageView>(R.id.processing_icon)
        val processingText = view.findViewById<TextView>(R.id.processing_text)
        val deliveryIcon = view.findViewById<ImageView>(R.id.delivery_icon)
        val deliveryText = view.findViewById<TextView>(R.id.delivery_text)

        val btn_cart = view?.findViewById<ImageView>(R.id.cart_icon)
        btn_cart?.setOnClickListener {
            val intent = Intent(activity, CartActivity::class.java)
            startActivity(intent)
        }

        orderRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        orderRecyclerView.adapter = orderAdapter
        orderBtn.setOnClickListener {
            val intent = Intent(activity, ProductActivity::class.java)
            startActivity(intent)
        }
        val emptyOrderLayout = view.findViewById<LinearLayout>(R.id.order_empty_layout)

        fun getData() {
            auth = Firebase.auth


            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        var order = data.getValue(Order::class.java)!!
                        orders.add(order)
                    }
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val uid = currentUser.uid

                        orders = orders.filter { order ->
                            order.quantity > "0" && order.idUser == uid
                        } as ArrayList<Order>
                    }
                    orderAdapter?.setItems(orders)
                        if (orders.isEmpty()) {
                            emptyOrderLayout!!.visibility = View.VISIBLE
                        } else {
                            emptyOrderLayout!!.visibility = View.GONE
                        }
                    Log.e("TAG", "onDataChange: ${orders.size}")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "loadPost:onCancelled", error.toException())
                }
            })
        }
        getData()
        return view
    }




    private fun addOrder(order: Order) {
        orders.add(order)
        orderAdapter.notifyDataSetChanged()
    }

    private fun removeAllOrder() {
        orders.clear()
        orderAdapter.notifyDataSetChanged()
    }

    private fun getStatus(status: String) {

    }

    private fun getAllOrder() {
        val orderList = ArrayList<Order>()

    }

    override fun onDeleteClick() {
        TODO("Not yet implemented")
    }

    override fun onUpdateClick() {
        TODO("Not yet implemented")
    }
}