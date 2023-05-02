package com.example.orderfood_app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R
import com.example.orderfood_app.adapters.OnItemClickListener
import com.example.orderfood_app.adapters.Order
import com.example.orderfood_app.adapters.OrderAdapter
import java.util.ArrayList


class OrderFragment : Fragment() {

    private lateinit var adapterOrder: OrderAdapter
    private lateinit var initListOrder: ArrayList<Order>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        val orderRecyclerView = view.findViewById<RecyclerView>(R.id.order_recycler_view)
        val emptyOrderLayout = view.findViewById<LinearLayout>(R.id.order_empty_layout)
        val orderBtn = view.findViewById<Button>(R.id.order_btn)
        val removeOrderBtn = view.findViewById<Button>(R.id.remove_all)
        val completeIcon = view.findViewById<ImageView>(R.id.complete_icon)
        val completeText = view.findViewById<TextView>(R.id.complete_text)
        val canceledIcon = view.findViewById<ImageView>(R.id.canceled_icon)
        val canceledText = view.findViewById<TextView>(R.id.canceled_text)
        val processingIcon = view.findViewById<ImageView>(R.id.processing_icon)
        val processingText = view.findViewById<TextView>(R.id.processing_text)
        val deliveryIcon = view.findViewById<ImageView>(R.id.delivery_icon)
        val deliveryText = view.findViewById<TextView>(R.id.delivery_text)

        emptyOrderLayout.visibility = View.GONE
        orderRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initListOrder = initListOrder()
        adapterOrder = OrderAdapter(initListOrder, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, initListOrder[position].date, Toast.LENGTH_SHORT).show()
            }
        })
        if (adapterOrder.itemCount == 0) {
            emptyOrderLayout.visibility = View.VISIBLE
        }
        adapterOrder.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (adapterOrder.itemCount == 0) {
                    emptyOrderLayout.visibility = View.VISIBLE
                } else {
                    emptyOrderLayout.visibility = View.GONE
                }

            }
        })
        orderRecyclerView.adapter = adapterOrder
        orderBtn.setOnClickListener {
            val order: Order = Order(1, "Đã giao", "Bánh mì ngon", "15.000 VND", "1 cái", "17/14/2023")
            addOrder(order)
        }
        removeOrderBtn.setOnClickListener {
            removeAllOrder()
        }
        return view


    }

    private fun initListOrder(): ArrayList<Order> {
        val list = ArrayList<Order>()
//        list.add(Order(1, "Đã giao", "Bánh mì ngon", "15.000 VND", "1 cái", "17/14/2023"))
//        list.add(Order(2, "Đã giao", "Bánh mì ngon", "15.000 VND", "1 cái", "17/14/2023"))
//        list.add(Order(3, "Đã giao", "Bánh mì ngon", "15.000 VND", "1 cái", "17/14/2023"))
//        list.add(Order(4, "Đã giao", "Bánh mì ngon", "15.000 VND", "1 cái", "17/14/2023"))
        return list
    }

    private fun addOrder(order: Order) {
        initListOrder.add(order)
        adapterOrder.notifyDataSetChanged()
    }
    private fun removeAllOrder() {
        initListOrder.clear()
        adapterOrder.notifyDataSetChanged()
    }
    private fun getStatus(status: String) {

    }
}