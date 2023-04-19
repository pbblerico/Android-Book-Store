package com.example.bookstore.fragments.admin.category

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentCategoryAdminBinding
import com.example.bookstore.fragments.admin.AdminManageFragment
import com.example.bookstore.models.AdapterCategory
import com.example.bookstore.models.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CategoryFragment : Fragment(R.layout.fragment_category_admin) {
    private lateinit var binding: FragmentCategoryAdminBinding
    private lateinit var cont: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryAdminBinding.inflate(inflater, container, false)

        insertNestedFragment()

        return binding.root
    }

    override fun onAttach(context: Context) {
        this.cont = context
        super.onAttach(context)
    }

    fun insertNestedFragment(childFragment: Fragment = SearchBarFragment()) {
        val transaction = childFragmentManager.beginTransaction();
        transaction.replace(R.id.category_frame, childFragment).addToBackStack(null).commit();
    }
}