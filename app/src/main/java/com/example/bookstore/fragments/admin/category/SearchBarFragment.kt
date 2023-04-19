package com.example.bookstore.fragments.admin.category

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentCategoryAdminBinding
import com.example.bookstore.databinding.FragmentSearchBarBinding
import com.example.bookstore.fragments.admin.AdminManageFragment
import com.example.bookstore.models.AdapterCategory
import com.example.bookstore.models.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchBarFragment : Fragment(R.layout.fragment_search_bar) {
    private lateinit var binding: FragmentSearchBarBinding
    private lateinit var cont: Context

    private lateinit var categoryArrayList: ArrayList<Category>
    private lateinit var adapterCategory: AdapterCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBarBinding.inflate(inflater, container, false)

        loadCategories()

        binding.addBtn.setOnClickListener { (parentFragment as CategoryFragment).insertNestedFragment(
            AddCategoryFragment()
        )}

        binding.categoryET.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterCategory.filter.filter(s)
                }
                catch (e: Exception) {

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        } )

        return binding.root
    }

    fun loadCategories() {
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()

                for(ds in snapshot.children) {
                    val model = ds.getValue(Category::class.java)

                    categoryArrayList.add(model!!)
                }

                adapterCategory = AdapterCategory(cont, categoryArrayList)

                binding.categoryView.adapter = adapterCategory
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