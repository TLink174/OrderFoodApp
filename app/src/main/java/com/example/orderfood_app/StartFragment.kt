package com.example.orderfood_app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        val btnsignin = view.findViewById<Button>(R.id.sign_in_button)
        btnsignin.setOnClickListener {
            val intent = Intent(activity, LoginFragment::class.java)
            startActivity(intent)
        }

        val btnsignup = view.findViewById<Button>(R.id.sign_up_button)
        btnsignup.setOnClickListener {
            val intent = Intent(activity, RegisterFragment::class.java)
            startActivity(intent)
        }
        return view
    }

}