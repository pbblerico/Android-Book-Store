package com.example.bookstore.fragments.authorization

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import com.example.bookstore.activity.MainActivity
import com.example.bookstore.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment: Fragment(R.layout.login_fragment), View.OnClickListener {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mainActivity: MainActivity
    private lateinit var cont: Context
    private val TAG = "LOGIN"

    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener(this)

        return binding.root
    }

    override fun onAttach(context: Context) {
        mainActivity = activity as MainActivity
        cont = context
        super.onAttach(context)
    }

    private fun validateData(): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(cont, "please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onClick(p0: View?) {
        this.email = binding.uname.text.toString().trim()
        this.password = binding.pass.text.toString().trim()

//        if (validateData())
            login()
    }

    private fun login() {
        auth.signInWithEmailAndPassword("karina@gmail.com", "12345678").addOnSuccessListener {
            Toast.makeText(cont, "Success", Toast.LENGTH_SHORT).show()
            checkUser()
            binding.uname.setText("")
            binding.pass.setText("")

            mainActivity.replaceMenu()
        }.addOnFailureListener { e ->
            Toast.makeText(cont, "Wrong email or password", Toast.LENGTH_SHORT).show()
            Log.e("TAG", "$e")
        }
    }

    private fun checkUser() {
        val fbUser = FirebaseAuth.getInstance().currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(fbUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                when (snapshot.child("userType").value) {
                    "User" -> Toast.makeText(cont, "User", Toast.LENGTH_SHORT).show()
                    "Admin" -> Toast.makeText(cont, "Admin", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}