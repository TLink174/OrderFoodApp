package com.example.orderfood_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.orderfood_app.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(
            inflater, container, false
        )
        // set onclick listener
        binding.signIn.setOnClickListener {
            println("Clicked")
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
        binding.signIn.setOnClickListener {view : View ->
//            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}