package com.example.orderfood_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Initialize Firebase Auth
        auth = Firebase.auth

        val btnSignUp = view.findViewById<TextView>(R.id.sign_up_ek1)
        btnSignUp.setOnClickListener {
            val newFragment = RegisterFragment()
            fragmentTransaction.replace(R.id.fragment_register, newFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        val btnSignIn = view.findViewById<Button>(R.id.sign_in_ek6)
        btnSignIn.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        val btnforgot = view.findViewById<TextView>(R.id.fogot_password)
        btnforgot.setOnClickListener {
            val newFragment = ResetpassFragment()
            fragmentTransaction.replace(R.id.fragment_resetpass, newFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val check_save = view.findViewById<CheckBox>(R.id.checkBox)
        return view
    }

    override fun onResume() {
        super.onResume()
        val sharePreference = activity?.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val email = sharePreference?.getString("email", null)
        val pass = sharePreference?.getString("password", null)
        val save = sharePreference?.getBoolean("save", false)
        val enter_your_email = view?.findViewById<TextView>(R.id.enter_your_email_ek1)
        val password = view?.findViewById<TextView>(R.id.password_ek1)
        if(save == true){
            enter_your_email?.setText(email)
            password?.setText(pass)
        }
    }

    private fun signinUser(context: Context){
        val email = view?.findViewById<TextView>(R.id.enter_your_email)?.text.toString()
        val pass = view?.findViewById<TextView>(R.id.password)?.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(context, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            // Tạo đối tượng User
            if (task.isSuccessful) {
                //lưu thông tin đăng nhập
                val sharePreference = activity?.getSharedPreferences("Login", Context.MODE_PRIVATE)
                val editor = sharePreference?.edit()
                //lưu theo dạng phân rã
                editor?.putString("email", email)
                editor?.putString("password", pass)
                editor?.putBoolean("save", view?.findViewById<CheckBox>(R.id.checkBox)?.isChecked ?: false)
                editor?.apply()

                Toast.makeText(context, "đã lưu thông tin đn", Toast.LENGTH_SHORT).show()

                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                val exception = task.exception
                val errorMessage = exception?.message
                Toast.makeText(context,"Authentication failed: $errorMessage",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
