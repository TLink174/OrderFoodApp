package com.example.orderfood_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.orderfood_app.R
import com.example.orderfood_app.models.Cart

interface OnItemClickListenerCart {
    //dùng hàm để cài đặt sự kiện cho nút trong item ở Activity
    fun onDeleteClick()
    fun onUpdateClick()
    fun onClickIncrease()
    fun onClickDecrease()
}

class CartAdapter(

) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private val carts = ArrayList<Cart>()
    private var listener: OnItemClickListenerCart? = null
    private var onClickView: ((Cart) -> Unit)? = null
    var count: Int? = null


    fun setOnItemClickListener(listener: OnItemClickListenerCart) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        var cart = carts[position]
        holder.setItem(cart)
//        holder.cartName.text = cart.name
//        holder.cartImage.load(cart.image)
//        holder.cartPrice.text = cart.price
//        holder.cartQuanlity.text = cart.quantity
        holder.setOnClickView {
            onClickView?.invoke(cart)
        }
        //cài đặt sự kiện cho nút trong item
        holder.delete.setOnClickListener {
            onClickView?.invoke(cart)
            listener?.onDeleteClick()
            notifyDataSetChanged()
        }
        holder.increaseButton.setOnClickListener {
            onClickView?.invoke(cart)
            count = cart.quantity.toInt()
            count = count!! + 1
            cart.quantity = count.toString()
            listener?.onClickIncrease()
            notifyDataSetChanged()
        }
        holder.decreaseButton.setOnClickListener {
            onClickView?.invoke(cart)
            count = cart.quantity.toInt()
            if (count!! > 1) {
                count = count!! - 1
                cart.quantity = count.toString()
            }
            listener?.onClickDecrease()
            notifyDataSetChanged()
        }
    }

    fun setItems(carts: ArrayList<Cart>) {
        this.carts.clear()
        this.carts.addAll(carts)
        notifyDataSetChanged()
        Log.e("CartAdapter", carts.size.toString())
    }

    fun setOnClickView(callback: (Cart) -> Unit) {
        this.onClickView = callback
    }


    override fun getItemCount(): Int = carts.size

    fun getCheckCart(): Boolean {
        return carts.size != 0
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var onClickView: ((Cart) -> Unit)? = null


        val increaseButton: Button = itemView.findViewById<Button>(R.id.increaseButton)
        val decreaseButton: Button = itemView.findViewById<Button>(R.id.decreaseButton)
        val delete: ImageView = itemView.findViewById<ImageView>(R.id.removeButton)
        val cartImage: ImageView = itemView.findViewById<ImageView>(R.id.cart_image)
        val cartName: TextView = itemView.findViewById<TextView>(R.id.cart_name)
        val cartPrice: TextView = itemView.findViewById<TextView>(R.id.cart_price)
        val cartQuantity: TextView = itemView.findViewById<TextView>(R.id.quantityEditText)
        fun setItem(cart: Cart) {
            cartName.text = cart.name
            cartImage.load(cart.image)
            cartPrice.text = cart.price
            cartQuantity.text = cart.quantity
        }

        fun setOnClickView(callback: (Cart) -> Unit) {
            this.onClickView = callback
        }


    }
}