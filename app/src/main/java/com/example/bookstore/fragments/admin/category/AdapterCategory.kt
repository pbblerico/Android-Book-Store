package com.example.bookstore.fragments.admin.category

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bookstore.activity.EditCategoryActivity
import com.example.bookstore.databinding.CategoryRowBinding
import com.example.bookstore.filters.FilterCategory
import com.example.bookstore.models.Category
import com.google.firebase.database.FirebaseDatabase

class AdapterCategory: Adapter<AdapterCategory.HolderCategory>, Filterable {
    private lateinit var binding: CategoryRowBinding
    private val context: Context
    var categoryArrayList: ArrayList<Category>
    private var filterList: ArrayList<Category>

    private var filter: FilterCategory? = null

    constructor(context: Context, categoryArrayList: ArrayList<Category>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = CategoryRowBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderCategory(binding.root)
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category
        val uid = model.uid
        val timestamp = model.timestamp

        holder.categoryTV.text = category

        holder.deleteBtn.setOnClickListener { delete(model, holder) }

        holder.updateBtn.setOnClickListener {
            val intent = Intent(context, EditCategoryActivity::class.java)
            intent.putExtra("categoryId", id)
            context.startActivity(intent)
        }
    }

    inner class HolderCategory(itemView: View): ViewHolder(itemView)   {
        var categoryTV: TextView = binding.categoryTV
        var deleteBtn: ImageButton = binding.deleteBtn
        var updateBtn: ImageButton = binding.updateBtn
    }

    fun delete(model: Category, holder: HolderCategory) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete")
            .setMessage("Are you sure you want to delete this category?")
            .setPositiveButton("Confirm"){a, d ->
                Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()
                deleteCategory(model, holder)
            }
            .setNegativeButton("Cancel") {a, d ->
                a.dismiss()
            }.show()
    }

    private fun deleteCategory(model: Category, holder: HolderCategory) {
        val id = model.id

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
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
            filter = FilterCategory(filterList, this)
        }
        return filter as FilterCategory
    }


}