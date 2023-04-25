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
import com.example.bookstore.databinding.FragmentCartBinding
import com.example.bookstore.databinding.FragmentShowBooksBinding
import com.example.bookstore.models.AdapterBookUser
import com.example.bookstore.models.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cont: Context

    private lateinit var bookArrayList: ArrayList<Book>
    private lateinit var adapterBookUser: AdapterCart


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        loadBooks()

        return binding.root
    }

    private fun loadBooks() {
        bookArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(FirebaseAuth.getInstance().uid!!).child("Cart")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookArrayList.clear()

                for(ds in snapshot.children) {
                    val bookId = "${ds.child("id").value}"

                    val book = Book()
                    book.id = bookId

                    bookArrayList.add(book)
                }

                adapterBookUser = AdapterCart(cont, bookArrayList)

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