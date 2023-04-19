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
import com.example.bookstore.databinding.SignupFragmentBinding
import com.example.bookstore.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment: Fragment(R.layout.signup_fragment), View.OnClickListener {
    private lateinit var binding: SignupFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mainActivity: MainActivity
    private lateinit var cont: Context
    private val TAG = "SIGNUP"

    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupFragmentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener(this)

        return binding.root
    }

    override fun onAttach(context: Context) {
        mainActivity = activity as MainActivity
        cont = context
        super.onAttach(context)
    }

    private fun signup() {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(cont, "Success", Toast.LENGTH_SHORT).show()
            createUser()
            binding.uname.setText("")
            binding.pass.setText("")

            mainActivity.replaceMenu()
        }.addOnFailureListener {e ->
            Toast.makeText(cont, "Failure", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "$e")
        }
    }

    private fun validateData(): Boolean {
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(cont, "please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.length < 6) {
            Toast.makeText(cont, "too short password, 6 character required", Toast.LENGTH_SHORT).show()
            return false
        }

        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
            if(it.result.signInMethods!!.size != 0) {
                Toast.makeText(cont, " user with such email already exists", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

    private fun createUser() {
        val timestamp = System.currentTimeMillis()

        val uid = auth.uid

        val newUser = User(email, password, timestamp, uid!!)

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid).setValue(newUser)
            .addOnSuccessListener {
                Toast.makeText(cont, "Success", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{e->
                Toast.makeText(cont, "Failure", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "$e")
            }

        email = ""
        password = ""
    }

    override fun onClick(p0: View?) {
        email = (binding.uname.text.toString()).trim()
        password = (binding.pass.text.toString()).trim()

        if(validateData()) signup()
    }
}

