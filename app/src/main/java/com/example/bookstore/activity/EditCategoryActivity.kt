package com.example.bookstore.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookstore.R
import com.example.bookstore.databinding.ActivityEditCategoryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBinding
    private var categoryId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryId = intent.getStringExtra("categoryId")!!
        loadCategory()

        binding.updateBtn.setOnClickListener { updateCategory() }
    }

    private fun updateCategory() {
        if(binding.categoryName.text.toString().isNotEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Update")
                .setMessage("Are you sure you want to update this category?")
                .setPositiveButton("Confirm"){a, d ->
                    Toast.makeText(this, "Updating", Toast.LENGTH_SHORT).show()
                    val ref = FirebaseDatabase.getInstance().getReference("Categories")
                    val hashMap = HashMap<String, Any>()
                    val newName = binding.categoryName.text.toString()
                    hashMap["category"] = "$newName"

                    ref.child(categoryId).updateChildren(hashMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                            binding.currName.text = newName
                            binding.categoryName.setText("")
                        }.addOnFailureListener{e ->
                            Toast.makeText(this, "error $e", Toast.LENGTH_SHORT).show()

                        }
                }
                .setNegativeButton("Cancel") {a, d ->
                    a.dismiss()
                }.show()
        }
    }

    fun loadCategory() {
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children) {
                    if(categoryId == ds.child("id").value) {
                        binding.currName.text = ds.child("category").value as String
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}