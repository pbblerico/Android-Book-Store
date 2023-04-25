package com.example.bookstore.fragments.authorization

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.bookstore.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseLogInImplementation {
    companion object {
        private var email = ""
        private var password = ""
        private lateinit var cont: Context
        private val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        private val TAG = "LOG_IN"

        fun login(email: String, password: String, cont: Context): Boolean {
            Companion.email = email
            Companion.password = password
            Companion.cont = cont

            var result = false
            if(validateData()) {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    Toast.makeText(cont, "Success", Toast.LENGTH_SHORT).show()
                    checkUser()
                    result = true
                }.addOnFailureListener {e ->
                    Toast.makeText(cont, "Wrong email or password", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "$e")
                }
            }
            return result
        }

        private fun checkUser() {
            val fbUser = FirebaseAuth.getInstance().currentUser!!

            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(fbUser.uid).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    when(snapshot.child("userType").value) {
                        "User" -> Toast.makeText(cont, "User", Toast.LENGTH_SHORT).show()
                        "Admin" -> Toast.makeText(cont, "Admin", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }


        private fun validateData(): Boolean {
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(cont, "please fill all fields", Toast.LENGTH_SHORT).show()
                return false
            }

            return true
        }
    }
}