package com.example.orderfood_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood_app.R
import com.example.orderfood_app.adapters.*

class SearchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val suggestRecyclerView = view.findViewById<RecyclerView>(R.id.suggest_recycler_view)
        suggestRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val initListSuggest = initListSuggest()
        suggestRecyclerView.adapter = SuggestAdapter(initListSuggest, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, initListSuggest[position].name, Toast.LENGTH_SHORT).show()
            }
        })

        val productSearchRecyclerView = view.findViewById<RecyclerView>(R.id.product_search_recycler_view)
        productSearchRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val initListProductSearch = initListProductSearch()
        productSearchRecyclerView.adapter = ProductSearchAdapter(initListProductSearch, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(context, initListProductSearch[position].name, Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }

    private fun initListProductSearch(): ArrayList<ProductSearch> {
        val list = ArrayList<ProductSearch>()
        list.add(ProductSearch(1, "Bánh mì", R.drawable.okokokok, "Hue", "15.000 VND"))
        list.add(ProductSearch(2, "Bánh tráng", R.drawable.okokokok, "Hue", "15.000 VND"))
        list.add(ProductSearch(3, "Bánh ngọt", R.drawable.okokokok, "Hue", "15.000 VND"))
        list.add(ProductSearch(4, "Bánh ngọt", R.drawable.okokokok, "Hue", "15.000 VND"))
        list.add(ProductSearch(5, "Bánh ngọt", R.drawable.okokokok, "Hue", "15.000 VND"))
        list.add(ProductSearch(6, "Bánh ngọt", R.drawable.okokokok, "Hue", "15.000 VND"))
        return list

    }

    private fun initListSuggest(): ArrayList<Suggest> {
        val list = ArrayList<Suggest>()
        list.add(Suggest(1, "Bánh mì"))
        list.add(Suggest(2, "Bánh tráng"))
        list.add(Suggest(3, "Bánh ngọt"))
        list.add(Suggest(4, "Bánh ngọt"))
        list.add(Suggest(5, "Bánh ngọt"))
        list.add(Suggest(6, "Bánh ngọt"))
        return list
    }


}