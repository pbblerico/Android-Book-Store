package com.example.bookstore.models

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.activity.BookDetailActivity
import com.example.bookstore.activity.CategoryDetailActivity
import com.example.bookstore.activity.EditCategoryActivity
import com.example.bookstore.databinding.CategoryRowBinding
import com.example.bookstore.databinding.CategoryRowUserBinding
import com.example.bookstore.filters.FilterCategory
import com.example.bookstore.filters.FilterCategoryUser
import com.google.firebase.database.FirebaseDatabase

class AdapterCategoryUser: RecyclerView.Adapter<AdapterCategoryUser.HolderCategoryUser>, Filterable {
    private lateinit var binding: CategoryRowUserBinding
    private val context: Context
    var categoryArrayList: ArrayList<Category>
    private var filterList: ArrayList<Category>

    private var filter: FilterCategoryUser? = null

    constructor(context: Context, categoryArrayList: ArrayList<Category>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoryUser {
        binding = CategoryRowUserBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderCategoryUser(binding.root)
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategoryUser, position: Int) {
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category
        val uid = model.uid
        val timestamp = model.timestamp

        holder.categoryTV.text = category

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CategoryDetailActivity::class.java)
            intent.putExtra("category", category)
            context.startActivity(intent)
        }
    }

    inner class HolderCategoryUser(itemView: View): RecyclerView.ViewHolder(itemView)   {
        var categoryTV: TextView = binding.categoryTV
    }


    override fun getFilter(): Filter {
        if(filter == null) {
            filter = FilterCategoryUser(filterList, this)
        }
        return filter as FilterCategoryUser
    }


}