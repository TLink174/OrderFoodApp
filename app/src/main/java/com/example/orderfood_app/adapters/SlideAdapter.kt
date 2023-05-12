package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.orderfood_app.R
import com.example.orderfood_app.models.Slide


class SlideAdapter(
    private val slides: ArrayList<Slide>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.slide_item, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {

        val slide = slides[position]
        holder.slideImage.load(slide.image)
//        holder.slideName.text = slide.name
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = slides.size


    class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slideImage: ImageView = itemView.findViewById<ImageView>(R.id.slide_image)
//        val slideName: TextView = itemView.findViewById<TextView>(R.id.name_slide)


    }

}