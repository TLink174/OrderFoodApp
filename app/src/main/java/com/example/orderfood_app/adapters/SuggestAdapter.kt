package com.example.orderfood_app.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R


data class Suggest(val id: Int, val name: String)
class SuggestAdapter(
    private val suggests: ArrayList<Suggest>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<SuggestAdapter.SuggestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.suggest_item, parent, false)
        return SuggestViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuggestViewHolder, position: Int) {

        val suggest = suggests[position]
//        holder.suggestImage.setImageResource(suggest.image)
        holder.suggestName.text = suggest.name
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }

    }

    override fun getItemCount(): Int = suggests.size


    class SuggestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val suggestName: TextView = itemView.findViewById<TextView>(R.id.suggest_name)
//        val suggestName: TextView = itemView.findViewById<TextView>(R.id.name_suggest)


    }

}