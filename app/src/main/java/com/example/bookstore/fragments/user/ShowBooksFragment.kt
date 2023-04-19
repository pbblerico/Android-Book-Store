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
import com.example.bookstore.databinding.FragmentShowBooksBinding
import com.example.bookstore.models.AdapterBookUser
import com.example.bookstore.models.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShowBooksFragment : Fragment(R.layout.fragment_show_books) {
    private lateinit var binding: FragmentShowBooksBinding
    private lateinit var cont: Context

    private lateinit var bookArrayList: ArrayList<Book>
    private lateinit var adapterBookUser: AdapterBookUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowBooksBinding.inflate(inflater, container, false)

        loadBooks()

        binding.bookET.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterBookUser.filter.filter(s)
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

                adapterBookUser = AdapterBookUser(cont, bookArrayList)

                binding.bookList.adapter = adapterBookUser
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