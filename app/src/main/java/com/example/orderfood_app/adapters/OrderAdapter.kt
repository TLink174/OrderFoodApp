package com.example.orderfood_app.adapters

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R
import com.example.orderfood_app.models.Cart
import com.example.orderfood_app.models.Order

interface OnItemClickListenerOrder {
    //dùng hàm để cài đặt sự kiện cho nút trong item ở Activity
    fun onDeleteClick()
    fun onUpdateClick()
}

class OrderAdapter() : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val orders = ArrayList<Order>()
    private var listener: OnItemClickListenerOrder? = null
    private var onClickView: ((Order) -> Unit)? = null

    fun setOnItemClickListener(listener: OnItemClickListenerOrder) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val order = orders[position]
//        holder.orderStatus.text = order.status
//        holder.orderLocation.text = order.loacation
//        holder.orderPrice.text = order.price
//        holder.orderQuanlity.text = order.quantity
//        holder.orderDate.text = order.date
//        holder.itemView.setOnClickListener {
//            listener.onItemClick(it, position)
//        }
        holder.setItem(order)
        holder.setOnClickView {
            onClickView?.invoke(order)
        }
    }

    fun setItems(orders: ArrayList<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
        notifyDataSetChanged()
        Log.e("CartAdapter", orders.size.toString())
    }

    fun setOnClickView(callback: (Order) -> Unit) {
        this.onClickView = callback
    }

    override fun getItemCount(): Int = orders.size

    fun getCheckOrder(): Boolean {
        return orders.size != 0
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var onClickView: ((Order) -> Unit)? = null

        val orderStatus: TextView = itemView.findViewById<TextView>(R.id.complete_text)
        val orderLocation: TextView = itemView.findViewById<TextView>(R.id.order_location)
        val orderUserName: TextView = itemView.findViewById<TextView>(R.id.order_user_name)
        val orderPrice: TextView = itemView.findViewById<TextView>(R.id.total_price)
        val orderQuanlity: TextView = itemView.findViewById<TextView>(R.id.total_quantity)
        val orderDate: TextView = itemView.findViewById<TextView>(R.id.order_date)
        fun setItem(order: Order) {
            orderStatus.text = order.status
            orderLocation.text = order.loacation
            orderPrice.text = order.price
            orderQuanlity.text = order.quantity
            orderDate.text = order.date
            orderUserName.text = order.username

        }

        fun setOnClickView(callback: (Order) -> Unit) {
            onClickView = callback
        }
    }

}