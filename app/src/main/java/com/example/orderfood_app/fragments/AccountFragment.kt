package com.example.orderfood_app.fragments

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.orderfood_app.LoginActivity
import com.example.orderfood_app.R
import com.example.orderfood_app.models.Cart
import com.example.orderfood_app.models.Category
import com.example.orderfood_app.models.Notification
import com.example.orderfood_app.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList

interface OnThemeChangeListener {
    fun onThemeChanged()
}

class AccountFragment : Fragment() {

    private var pickedPhoto: Uri? = null
    private var pickedBitmap: Bitmap? = null
    private var users: ArrayList<User> = ArrayList()
    private lateinit var databaseReference: DatabaseReference


    private lateinit var auth: FirebaseAuth
    private var onClickView: ((User) -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().getReference("user")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        fun setOnClickView(callback: (User) -> Unit) {
            this.onClickView = callback
        }
        val name = view.findViewById<TextView>(R.id.account_name)
        val emailText = view.findViewById<TextView>(R.id.account_email)
        val birthday = view.findViewById<TextView>(R.id.account_date)
        val age = view.findViewById<TextView>(R.id.account_age)
        val edit = view.findViewById<Button>(R.id.edit_btn)
        val editname = view.findViewById<EditText>(R.id.edit_name)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val email = currentUser.email
            emailText.text = email
            val currentDate = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthdayDate = dateFormat.parse(birthday.text.toString())
            val birthdayCalendar = Calendar.getInstance()
            birthdayCalendar.time = birthdayDate

            var ageValue = currentDate.get(Calendar.YEAR) - birthdayCalendar.get(Calendar.YEAR)

// Kiểm tra nếu tháng và ngày hiện tại nhỏ hơn tháng và ngày sinh, giảm 1 tuổi
            if (currentDate.get(Calendar.MONTH) < birthdayCalendar.get(Calendar.MONTH) ||
                (currentDate.get(Calendar.MONTH) == birthdayCalendar.get(Calendar.MONTH) &&
                        currentDate.get(Calendar.DAY_OF_MONTH) < birthdayCalendar.get(Calendar.DAY_OF_MONTH))) {
                ageValue--
            }
            Log.e("AccountFragment", "Age: $ageValue")
            age.text = ageValue.toString()

            // Lấy thông tin người dùng từ Realtime Database
            val databaseReference = FirebaseDatabase.getInstance().getReference("user")
            getUserById(uid)
            birthday.setOnClickListener {
                val currentDate = Calendar.getInstance()
                val year = currentDate.get(Calendar.YEAR)
                val month = currentDate.get(Calendar.MONTH)
                val day = currentDate.get(Calendar.DAY_OF_MONTH)

                val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    // Xử lý khi ngày được chọn
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    birthday.text = formattedDate

                    val birthdayDate = dateFormat.parse(formattedDate)
                    val birthdayCalendar = Calendar.getInstance()
                    birthdayCalendar.time = birthdayDate

                    var ageValue = currentDate.get(Calendar.YEAR) - birthdayCalendar.get(Calendar.YEAR)

// Kiểm tra nếu tháng và ngày hiện tại nhỏ hơn tháng và ngày sinh, giảm 1 tuổi
                    if (currentDate.get(Calendar.MONTH) < birthdayCalendar.get(Calendar.MONTH) ||
                        (currentDate.get(Calendar.MONTH) == birthdayCalendar.get(Calendar.MONTH) &&
                                currentDate.get(Calendar.DAY_OF_MONTH) < birthdayCalendar.get(Calendar.DAY_OF_MONTH))) {
                        ageValue--
                    }
                    Log.e("AccountFragment", "Age: $ageValue")
                    age.text = ageValue.toString()
                }, year, month, day)
                datePicker.datePicker.maxDate = System.currentTimeMillis() -86400000
                datePicker.show()
            }
            databaseReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        // Thực hiện xử lý với thông tin người dùng tại đây
                        Log.d("AccountFragment", "User name: ${user.name}")
                        Log.d("AccountFragment", "User email: ${user.email}")
                        name.text = "${user.name}"
                        emailText.text = "${user.email}"
                        birthday.text = "${user.birthday}"
                        editname.setText(user.name)
                        // ...
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("AccountFragment", "Error retrieving user data", error.toException())
                }
            })

        val logOut = view.findViewById<Button>(R.id.logOut_btn)
        logOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        edit.setOnClickListener {
            val userData = User(
                "${uid}",
                "${editname.text}",
                "${emailText.text}",
                "",
                "${birthday.text}",
            )

                        databaseReference.child(uid).setValue(userData)
                            .addOnSuccessListener {
                                // Thành công
                                Toast.makeText(context, "Dữ liệu đã được lưu trữ thành công.", Toast.LENGTH_SHORT).show()
                                name.text = "${editname.text}"
                            }
                            .addOnFailureListener {
                                // Xảy ra lỗi
                                Toast.makeText(context, "Lưu trữ dữ liệu thất bại.", Toast.LENGTH_SHORT).show()
                            }
            }
        }
        return view
    }

    fun getUserById(userId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("user")

        databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    // Thực hiện xử lý với thông tin người dùng tại đây
                    Log.d("AccountFragment", "User name: ${user.name}")
                    Log.d("AccountFragment", "User email: ${user.email}")
                    // ...
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AccountFragment", "Error retrieving user data", error.toException())
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnPickPhoto = view.findViewById<ImageView>(R.id.avatar)

        btnPickPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageView = requireView().findViewById<ImageView>(R.id.avatar)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, pickedPhoto!!)
                    pickedBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(pickedBitmap)
                } else {
                    pickedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, pickedPhoto)
                    imageView.setImageBitmap(pickedBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
