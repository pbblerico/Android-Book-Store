package com.example.bookstore.fragments.authorization

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import com.example.bookstore.activity.MainActivity
import com.example.bookstore.databinding.SignupFragmentBinding

class SignUpFragment: Fragment(R.layout.signup_fragment), View.OnClickListener {
    private lateinit var binding: SignupFragmentBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var cont: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupFragmentBinding.inflate(inflater, container, false)

        binding.loginBtn.setOnClickListener(this)

        return binding.root
    }

    override fun onAttach(context: Context) {
        mainActivity = activity as MainActivity
        cont = context
        super.onAttach(context)
    }

    private fun signup() {
        val email = (binding.uname.text.toString()).trim()
        val password = (binding.pass.text.toString()).trim()

        if(FirebaseSignUpImplementation.signup(email, password, cont)) {
            binding.uname.setText("")
            binding.pass.setText("")

            mainActivity.replaceMenu()
        }
    }

    override fun onClick(p0: View?) {
        signup()
    }
}

