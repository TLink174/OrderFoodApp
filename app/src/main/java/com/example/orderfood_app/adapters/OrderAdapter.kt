package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R

interface OnItemClickListenerOrder {
    //dùng hàm để cài đặt sự kiện cho nút trong item ở Activity
    fun onDeleteClick()
    fun onUpdateClick()
}

data class Order(val id: Int, val status: String, val loacation: String, val price: String, val quantity: String, val date: String)
class OrderAdapter(
    private val orders: ArrayList<Order>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val order = orders[position]
        holder.orderStatus.text = order.status
        holder.orderLocation.text = order.loacation
        holder.orderPrice.text = order.price
        holder.orderQuanlity.text = order.quantity
        holder.orderDate.text = order.date
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = orders.size

    fun getCheckOrder(): Boolean {
        return orders.size != 0
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderStatus: TextView = itemView.findViewById<TextView>(R.id.complete_text)
        val orderLocation: TextView = itemView.findViewById<TextView>(R.id.order_resturant_name)
        val orderPrice: TextView = itemView.findViewById<TextView>(R.id.total_price)
        val orderQuanlity: TextView = itemView.findViewById<TextView>(R.id.total_quantity)
        val orderDate: TextView = itemView.findViewById<TextView>(R.id.order_date)


    }

}