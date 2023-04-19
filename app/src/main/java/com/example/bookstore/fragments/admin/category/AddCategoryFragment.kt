package com.example.bookstore.fragments.admin.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentAddCategoryBinding
import com.example.bookstore.fragments.admin.AdminManageFragment
import com.example.bookstore.models.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddCategoryFragment : Fragment(R.layout.fragment_add_category) {
    private lateinit var binding: FragmentAddCategoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var cont: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.addBtn.setOnClickListener { addNewCategory() }
        binding.btnReturn.setOnClickListener {(parentFragment as CategoryFragment).insertNestedFragment(
            SearchBarFragment()
        )}

        return binding.root
    }

    override fun onAttach(context: Context) {
        this.cont = context
        super.onAttach(context)
    }

    private fun addNewCategory() {
        val category = binding.categoryAdd.text.toString().trim()
        if(category.isEmpty()) {
            Toast.makeText(cont, "Empty category", Toast.LENGTH_SHORT).show()
            return
        }

        val timestamp = System.currentTimeMillis()

        val newCategory = Category("$timestamp", category, timestamp, "${auth.uid}")

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child("$timestamp").setValue(newCategory)
            .addOnSuccessListener {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
    }
}