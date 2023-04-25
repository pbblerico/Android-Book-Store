package com.example.bookstore.fragments.user

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookstore.R
import com.example.bookstore.activity.MainActivity
import com.example.bookstore.activity.UserActivity
import com.example.bookstore.databinding.FragmentCategoryUserBinding
import com.example.bookstore.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var cont: Context
    private lateinit var userActivity: UserActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.logout.setOnClickListener { userActivity.finishActivity() }

        return binding.root
    }

    override fun onAttach(context: Context) {
        userActivity = activity as UserActivity
        this.cont = context
        super.onAttach(context)
    }
}