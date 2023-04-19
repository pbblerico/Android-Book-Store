package com.example.bookstore.models

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.activity.BookDetailActivity
import com.example.bookstore.databinding.BookRowUserBinding
import com.example.bookstore.filters.FilterBookUser
import com.google.firebase.database.ValueEventListener

class AdapterBookUser: RecyclerView.Adapter<AdapterBookUser.HolderBookUser>, Filterable {
    private lateinit var binding: BookRowUserBinding
    private var context: Context
    var bookArrayList: ArrayList<Book>
    private var filterList: ArrayList<Book>

    private var filter: FilterBookUser? = null

    constructor(context: Context, bookArrayList: ArrayList<Book>) {
        this.context = context
        this.bookArrayList = bookArrayList
        this.filterList = bookArrayList
    }

    inner class HolderBookUser(itemView: View): RecyclerView.ViewHolder(itemView) {
        var name: TextView = binding.bookName
        var author: TextView = binding.bookAuth
        var category: TextView = binding.bookCat
        var cost: TextView = binding.bookCost
        var desc: TextView = binding.bookDesc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBookUser {
        binding = BookRowUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderBookUser(binding.root)
    }

    override fun getItemCount(): Int {
        return bookArrayList.size
    }

    override fun onBindViewHolder(holder: HolderBookUser, position: Int) {
        val model = bookArrayList[position]
        val name = model.bookName
        val id = model.id
        val category = model.category
        val cost = model.cost
        val desc = model.desc
        val author = model.author
        val timestamp = model.timestamp

        holder.name.text = name
        holder.author.text = author
        holder.category.text = category
        holder.cost.text = cost.toString()
        holder.desc.text = desc

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra("bookId", id)
            context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        if(filter == null) {
            filter = FilterBookUser(filterList, this)
        }
        return filter as FilterBookUser
    }
}