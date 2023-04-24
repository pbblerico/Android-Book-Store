package com.example.bookstore.fragments.authorization

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.bookstore.models.User
import com.google.firebase.database.FirebaseDatabase

class FirebaseSignUpImplementation {

    companion object {
        private var email = ""
        private var password = ""
        private val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        private val TAG = "SIGN_UP"

        fun signup(email: String, password: String, cont: Context): Boolean {
            Companion.email = email
            Companion.password = password

            if(validateData(cont)) {
                var result = false
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    Toast.makeText(cont, "Success", Toast.LENGTH_SHORT).show()
                    createUser(cont)
                    result = true
                }.addOnFailureListener {e ->
                    Toast.makeText(cont, "Failure", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "$e")
                }
                return result
            }
            return false
        }

        private fun createUser(cont: Context) {
            val timestamp = System.currentTimeMillis()

            val uid = auth.uid

            val newUser = User(email, password, timestamp, uid!!)

            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(uid).setValue(newUser)
                .addOnSuccessListener {
                    Toast.makeText(cont, "Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{e->
                    Toast.makeText(cont, "Failure $e", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "$e")
                }
        }

        private fun validateData(cont: Context): Boolean {
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(cont, "please fill all fields", Toast.LENGTH_SHORT).show()
                return false
            }
            if(password.length < 6) {
                Toast.makeText(cont, "too short password, 6 character required", Toast.LENGTH_SHORT).show()
                return false
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(cont, "bad email address", Toast.LENGTH_SHORT).show()
                return false
            }

            auth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
                if(it.result.signInMethods!!.size != 0) {
                    Toast.makeText(cont, " user with such email already exists", Toast.LENGTH_SHORT).show()
                }
            }

            return true
        }
    }
}