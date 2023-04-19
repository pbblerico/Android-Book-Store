package com.example.bookstore.models

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.activity.BookDetailActivity
import com.example.bookstore.activity.EditBookActivity
import com.example.bookstore.activity.EditCategoryActivity
import com.example.bookstore.activity.UserActivity
import com.example.bookstore.databinding.BookRowBinding
import com.example.bookstore.databinding.CategoryRowBinding
import com.example.bookstore.filters.FilterBook
import com.example.bookstore.filters.FilterCategory
import com.google.firebase.database.FirebaseDatabase

class AdapterBook: Adapter<AdapterBook.HolderBook>, Filterable{
    private lateinit var binding: BookRowBinding
    private var context: Context
    var bookArrayList: ArrayList<Book>
    private var filterList: ArrayList<Book>

    private var filter: FilterBook? = null

    constructor(context: Context, bookArrayList: ArrayList<Book>) {
        this.context = context
        this.bookArrayList = bookArrayList
        this.filterList = bookArrayList
    }

    inner class HolderBook(itemView: View): ViewHolder(itemView) {
        var name: TextView = binding.bookName
        var author: TextView = binding.bookAuth
        var category: TextView = binding.bookCat
        var cost: TextView = binding.bookCost
        var desc: TextView = binding.bookDesc
        var deleteBtn: ImageButton = binding.deleteBtn
        var updateBtn: ImageButton = binding.updateBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBook {
        binding = BookRowBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderBook(binding.root)
    }

    override fun getItemCount(): Int {
        return bookArrayList.size
    }

    override fun onBindViewHolder(holder: HolderBook, position: Int) {
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

        holder.deleteBtn.setOnClickListener {  delete(model, holder) }

        holder.updateBtn.setOnClickListener {
            val intent = Intent(context, EditBookActivity::class.java)
            intent.putExtra("bookId", id)
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra("bookId", id)
            context.startActivity(intent)
        }
    }

    fun delete(model: Book, holder: AdapterBook.HolderBook) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete")
            .setMessage("Are you sure you want to delete this category?")
            .setPositiveButton("Confirm"){a, d ->
                Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()
                deleteBook(model, holder)
            }
            .setNegativeButton("Cancel") {a, d ->
                a.dismiss()
            }.show()
    }

    private fun deleteBook(model: Book, holder: AdapterBook.HolderBook) {
        val id = model.id

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e ->
                Toast.makeText(context, "Unable to delete due to $e", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getFilter(): Filter {
        if(filter == null) {
            filter = FilterBook(filterList, this)
        }
        return filter as FilterBook
    }
}