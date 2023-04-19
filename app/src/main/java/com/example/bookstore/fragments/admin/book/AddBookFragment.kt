package com.example.bookstore.fragments.admin.book

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentAddBookBinding
import com.example.bookstore.databinding.FragmentAddCategoryBinding
import com.example.bookstore.fragments.admin.category.CategoryFragment
import com.example.bookstore.fragments.admin.category.SearchBarFragment
import com.example.bookstore.models.Book
import com.example.bookstore.models.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddBookFragment : Fragment(R.layout.fragment_add_book) {
    private lateinit var binding: FragmentAddBookBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var cont: Context

    private lateinit var categoryArrayList: ArrayList<Category>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBookBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        loadCategories()

        binding.category.setOnClickListener { categoryPickDialog() }
        binding.sbmtBtn.setOnClickListener { addNewBook() }
        binding.btnReturn.setOnClickListener {(parentFragment as BookFragment).insertNestedFragment(
            SearchBookFragment()
        )}

        return binding.root
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

    override fun onAttach(context: Context) {
        this.cont = context
        super.onAttach(context)
    }

    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""

    private fun categoryPickDialog() {
        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for(i in categoriesArray.indices) {
            categoriesArray[i] = categoryArrayList[i].category
        }

        val builder = AlertDialog.Builder(cont)
        builder.setTitle("Pick Category")
            .setItems(categoriesArray) {dialog, which ->
                selectedCategoryId = categoryArrayList[which].id
                selectedCategoryTitle = categoryArrayList[which].category

                binding.category.text = selectedCategoryTitle
            }.show()

    }

    private fun addNewBook() {
        val name = binding.bookName.text.toString().trim()
        val desc = binding.desc.text.toString().trim()
        var cost = binding.cost.text.toString().trim()
        val author = binding.author.text.toString().trim()
        val category = binding.category.text.toString().trim()
        if(name.isEmpty() || author.isEmpty() || cost.isEmpty()) {
            Toast.makeText(cont, "Empty fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            cost.toDouble()
        } catch (e: Exception) {
            Toast.makeText(cont, "Wrong value for cost", Toast.LENGTH_SHORT).show()
            return
        }

        val timestamp = System.currentTimeMillis()

        val newBook = Book("$timestamp", name, desc, author, cost.toDouble(), category, timestamp, "${auth.uid}")

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child("$timestamp").setValue(newBook)
            .addOnSuccessListener {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }

        binding.author.setText("")
        binding.bookName.setText("")
        binding.category.setText("category")
        binding.cost.setText("")
        binding.desc.setText("")

    }

}