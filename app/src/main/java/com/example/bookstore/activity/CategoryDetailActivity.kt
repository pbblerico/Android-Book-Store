package com.example.bookstore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.bookstore.databinding.ActivityCategoryDetailBinding
import com.example.bookstore.models.AdapterBookUser
import com.example.bookstore.models.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryDetailBinding
    private var category = ""
    
    private lateinit var bookList: ArrayList<Book>
    private lateinit var adapterBookUser: AdapterBookUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getStringExtra("category")!!
        binding.book.text = category

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


    }

    private fun loadBooks() {
        bookList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookList.clear()

                for (ds in snapshot.children) {
                    if (category == ds.child("category").value) {
                        val book = ds.getValue(Book::class.java)!!

                        bookList.add(book!!)
                    }
                }

                adapterBookUser = AdapterBookUser(this@CategoryDetailActivity, bookList)

                binding.bookList.adapter = adapterBookUser
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}