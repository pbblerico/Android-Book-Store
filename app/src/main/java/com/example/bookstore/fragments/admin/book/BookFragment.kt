package com.example.bookstore.fragments.admin.book

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentBookBinding
import com.example.bookstore.fragments.admin.category.SearchBarFragment
import com.example.bookstore.models.AdapterBook
import com.example.bookstore.models.Book
import com.example.bookstore.models.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BookFragment : Fragment(R.layout.fragment_book) {
    private lateinit var binding: FragmentBookBinding
    private lateinit var cont: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false)

        insertNestedFragment()


        return binding.root
    }

    fun insertNestedFragment(childFragment: Fragment = SearchBookFragment()) {
        val transaction = childFragmentManager.beginTransaction();
        transaction.replace(R.id.book_view, childFragment).addToBackStack(null).commit();
    }

    override fun onAttach(context: Context) {
        this.cont = context
        super.onAttach(context)
    }
}