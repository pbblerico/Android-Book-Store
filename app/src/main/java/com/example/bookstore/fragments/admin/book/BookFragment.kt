package com.example.bookstore.fragments.admin.book

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentBookBinding


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