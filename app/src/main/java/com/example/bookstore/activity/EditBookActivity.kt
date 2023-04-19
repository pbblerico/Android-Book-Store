package com.example.bookstore.activity

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookstore.R
import com.example.bookstore.databinding.ActivityEditBookAcitivityBinding
import com.example.bookstore.models.Book
import com.example.bookstore.models.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBookAcitivityBinding
    private var bookId = ""
    private lateinit var book: Book

    private lateinit var categoryArrayList: ArrayList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBookAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("bookId")!!
        loadBook()

        loadCategories()

        binding.category.setOnClickListener { categoryPickDialog() }
        binding.updateBtn.setOnClickListener { updateBook() }
    }

    private fun updateBook() {
        var name = binding.bookName.text.toString().trim()
        var desc = binding.desc.text.toString().trim()
        var author = binding.author.text.toString().trim()
        var cost = binding.cost.text.toString().trim()
        var cat = binding.category.text.toString().trim()

        if(name.isEmpty()) name = book.bookName
        if(desc.isEmpty()) desc = book.desc
        if(author.isEmpty()) author = book.author
        if(cost.isEmpty()) cost = book.cost.toString()
        try {
            cost.toDouble()
        } catch (e: Exception) {
            Toast.makeText(this, "Wrong value for cost", Toast.LENGTH_SHORT).show()
            return
        }

        if(cat.isEmpty()) cat = book.category

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update")
            .setMessage("Are you sure you want to update this book?")
            .setPositiveButton("Confirm"){a, d ->
                Toast.makeText(this, "Updating", Toast.LENGTH_SHORT).show()
                val ref = FirebaseDatabase.getInstance().getReference("Books")
                val hashMap = HashMap<String, Any>()
                hashMap["category"] = "$cat"
                hashMap["bookName"] = "$name"
                hashMap["cost"] = cost.toDouble()
                hashMap["desc"] = desc
                hashMap["author"] = author

                ref.child(bookId).updateChildren(hashMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                        binding.book.text = name
                        binding.bookAuth.text = author
                        binding.bookCat.text = cat
                        binding.bookCost.text = cost
                        binding.bookDesc.text = desc
                    }.addOnFailureListener{e ->
                        Toast.makeText(this, "error $e", Toast.LENGTH_SHORT).show()

                    }
            }
            .setNegativeButton("Cancel") {a, d ->
                a.dismiss()
            }.show()
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

    private fun loadCategories() {
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for(ds in snapshot.children) {
                    val model = ds.getValue(Category::class.java)

                    categoryArrayList.add(model!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""

    private fun categoryPickDialog() {
        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for(i in categoriesArray.indices) {
            categoriesArray[i] = categoryArrayList[i].category
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(categoriesArray) {dialog, which ->
                selectedCategoryId = categoryArrayList[which].id
                selectedCategoryTitle = categoryArrayList[which].category

                binding.category.text = selectedCategoryTitle
            }.show()

    }
}