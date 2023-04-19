package com.example.bookstore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookstore.R
import com.example.bookstore.databinding.ActivityBookDetailBinding
import com.example.bookstore.models.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    private var bookId = ""
    private lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("bookId")!!


        loadBook()
    }

    private fun loadBook() {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children) {
                    if(bookId == ds.child("id").value) {
                        book = ds.getValue(Book::class.java)!!
                        binding.book.text = book.bookName
                        binding.bookAuth.text = book.author
                        binding.bookCat.text = book.category
                        binding.bookCost.text = book.cost.toString()
                        binding.bookDesc.text = book.desc
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}