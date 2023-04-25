package com.example.bookstore.fragments.user

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.activity.BookDetailActivity
import com.example.bookstore.databinding.BookRowCartBinding
import com.example.bookstore.databinding.BookRowUserBinding
import com.example.bookstore.filters.FilterBookUser
import com.example.bookstore.fragments.admin.book.AdapterBook
import com.example.bookstore.models.AdapterBookUser
import com.example.bookstore.models.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdapterCart : RecyclerView.Adapter<AdapterCart.HolderCart> {
    private lateinit var binding: BookRowCartBinding
    private var context: Context
    var bookArrayList: ArrayList<Book>

    constructor(context: Context, bookArrayList: ArrayList<Book>) {
        this.context = context
        this.bookArrayList = bookArrayList
    }

    inner class HolderCart(itemView: View): RecyclerView.ViewHolder(itemView) {
        var name: TextView = binding.bookName
        var author: TextView = binding.bookAuth
        var category: TextView = binding.bookCat
        var cost: TextView = binding.bookCost
        var desc: TextView = binding.bookDesc
        var addToCart: ImageButton = binding.cart
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCart {
        binding = BookRowCartBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCart(binding.root)
    }

    override fun getItemCount(): Int {
        return bookArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCart, position: Int) {
        val model = bookArrayList[position]
        val id = model.id

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = "${snapshot.child("bookName").value}"
                    val category = "${snapshot.child("category").value}"
                    val cost = "${snapshot.child("cost").value}"
                    val desc = "${snapshot.child("desc").value}"
                    val author = "${snapshot.child("author").value}"

                    holder.name.text = name
                    holder.author.text = author
                    holder.category.text = category
                    holder.cost.text = cost
                    holder.desc.text = desc
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        holder.itemView.setOnClickListener {
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra("bookId", id)
            context.startActivity(intent)
        }

        holder.addToCart.setOnClickListener {
            delete(model, holder)
        }


    }

    fun delete(model: Book, holder: AdapterCart.HolderCart) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete")
            .setMessage("Are you sure you want to remove this book from favourites?")
            .setPositiveButton("Confirm"){a, d ->
                Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()
                removeFromCart(model, holder)
            }
            .setNegativeButton("Cancel") {a, d ->
                a.dismiss()
            }.show()
    }

    private fun removeFromCart(model: Book, holder: AdapterCart.HolderCart) {
        val id = model.id

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(FirebaseAuth.getInstance().uid!!).child("Cart").child(id)
            .removeValue() .addOnSuccessListener {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e ->
                Toast.makeText(context, "Unable to delete due to $e", Toast.LENGTH_SHORT).show()
            }
    }
}