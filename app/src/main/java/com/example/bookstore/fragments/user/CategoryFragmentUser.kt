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
import com.example.bookstore.databinding.FragmentCategoryUserBinding
import com.example.bookstore.models.AdapterCategoryUser
import com.example.bookstore.models.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Collections


class CategoryFragmentUser : Fragment(R.layout.fragment_category_user) {
    private lateinit var binding: FragmentCategoryUserBinding
    private lateinit var cont: Context

    private lateinit var categoryArrayList: ArrayList<Category>
    private lateinit var adapterCategoryUser: AdapterCategoryUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryUserBinding.inflate(inflater, container, false)

        loadCategories()

        binding.categoryET.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterCategoryUser.filter.filter(s)
                }
                catch (e: Exception) {

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        } )

//        binding.sortBtn.setOnClickListener {  }

        return binding.root
    }

    private fun loadCategories() {
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()

                for(ds in snapshot.children) {
                    val model = ds.getValue(Category::class.java)

                    categoryArrayList.add(model!!)
                }

                adapterCategoryUser = AdapterCategoryUser(cont, categoryArrayList)

                binding.categoryView.adapter = adapterCategoryUser
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