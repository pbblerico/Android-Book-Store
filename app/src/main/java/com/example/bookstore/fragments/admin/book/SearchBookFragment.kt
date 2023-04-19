package com.example.bookstore.fragments.admin.book

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentSearchBarBinding
import com.example.bookstore.databinding.FragmentSearchBookBinding
import com.example.bookstore.fragments.admin.category.AddCategoryFragment
import com.example.bookstore.fragments.admin.category.CategoryFragment
import com.example.bookstore.models.AdapterBook
import com.example.bookstore.models.AdapterCategory
import com.example.bookstore.models.Book
import com.example.bookstore.models.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchBookFragment : Fragment(R.layout.fragment_search_book) {
    private lateinit var binding: FragmentSearchBookBinding
    private lateinit var cont: Context

    private lateinit var adapterBook: AdapterBook
    private lateinit var bookArrayList: ArrayList<Book>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBookBinding.inflate(inflater, container, false)

        loadBooks()

        binding.addBtn.setOnClickListener { (parentFragment as BookFragment).insertNestedFragment(
            AddBookFragment()
        )}

        binding.bookET.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterBook.filter.filter(s)
                }
                catch (e: Exception) {

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        } )


        return binding.root
    }



        private fun loadBooks() {
            bookArrayList = ArrayList()

            val ref = FirebaseDatabase.getInstance().getReference("Books")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bookArrayList.clear()

                    for(ds in snapshot.children) {
                        val model = ds.getValue(Book::class.java)

                        bookArrayList.add(model!!)
                    }

                    adapterBook = AdapterBook(cont, bookArrayList)

                    binding.bookList.adapter = adapterBook
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    override fun onAttach(context: Context) {
        this.cont = context
        super.onAttach(context)
    }
}