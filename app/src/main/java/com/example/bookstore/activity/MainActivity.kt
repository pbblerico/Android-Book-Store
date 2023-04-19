package com.example.bookstore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import com.example.bookstore.fragments.authorization.LoginFragment
import com.example.bookstore.fragments.authorization.SignUpFragment
import com.example.bookstore.databinding.ActivityMainBinding
import com.example.bookstore.enums.UserState

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MAIN_ACT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPageSwitch()
    }

    private fun initPageSwitch() {
        binding.bottomNavigationView.setOnItemSelectedListener{
            val fragment = when(it.itemId) {
                R.id.login_menu -> { LoginFragment() }
                else -> { SignUpFragment() }
            }
            replaceFragment(fragment)
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, userState: UserState = UserState.NOT_LOGGED_IN) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.start_page,fragment)
        fragmentTransaction.commit()
    }

    fun replaceMenu() {
        startActivity(Intent(this, UserActivity::class.java))
        finish()
    }
}