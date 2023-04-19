package com.example.bookstore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import com.example.bookstore.databinding.ActivityUserBinding
import com.example.bookstore.enums.UserState
import com.example.bookstore.fragments.admin.AdminManageFragment
import com.example.bookstore.fragments.authorization.InitFragment
import com.example.bookstore.fragments.user.CategoryFragmentUser
import com.example.bookstore.fragments.user.ShowBooksFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var auth: FirebaseAuth
    var userType = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setBottomNavBar()

        bottomNavBar()
    }

    private fun setBottomNavBar() {
        val fbUser = FirebaseAuth.getInstance().currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(fbUser.uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                when(snapshot.child("userType").value) {
                    "User" -> binding.bottomNavigationView.inflateMenu(R.menu.user_bottom_nav_menu)
                    "Admin" -> {
                        binding.bottomNavigationView.inflateMenu(R.menu.admin_bottom_nav_menu)
                        userType = "Admin"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun bottomNavBar() {
        binding.bottomNavigationView.setOnItemSelectedListener{
            val fragment = when(userType) {
                "Admin" -> { adminBottomNavBar(it) }
                else -> { userBottomNavBar(it)}
            } as Fragment
            replaceFragment(fragment)
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, userState: UserState = UserState.NOT_LOGGED_IN) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.start_page,fragment).addToBackStack(null).commit()
    }

    private fun adminBottomNavBar(it: MenuItem): Fragment {
        val fragment = when(it.itemId) {
            R.id.manage -> AdminManageFragment()
            R.id.books -> ShowBooksFragment()
            else -> InitFragment()
        }
        return fragment
    }

    private fun userBottomNavBar(it: MenuItem): Fragment {
        val fragment = when(it.itemId) {
//            R.id.manage -> AdminManageFragment()
            R.id.catalog -> CategoryFragmentUser()
            R.id.home -> ShowBooksFragment()
            else -> InitFragment()
        }
        return fragment
    }
}