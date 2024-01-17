package com.example.businesscards

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.businesscards.databinding.ActivityViewDataBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewDataActivity : AppCompatActivity(), BusinessCardAdapter.OnDeleteClickListener {

    private lateinit var binding: ActivityViewDataBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var businessCardList: MutableList<BusinessCard>
    private lateinit var adapter: BusinessCardAdapter // Create an adapter for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        businessCardList = mutableListOf()

        // Provide onDeleteClickListener when creating UserAdapter
        adapter = BusinessCardAdapter(businessCardList, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Retrieve data from Firebase
        val reference = database.getReference("BusinessCard")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                businessCardList.clear()
                for (dataSnapshot in snapshot.children) {
                    val businessCard = dataSnapshot.getValue(BusinessCard::class.java)
                    if (businessCard != null) {
                        businessCardList.add(businessCard)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        var actionBar = supportActionBar

        // showing the back button in the action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            // Customize the back button
            actionBar.setHomeAsUpIndicator(R.drawable.logout)
        }

        // Set click listener for the upload button
        binding.addButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ViewDataActivity, UploadActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    override fun onDeleteClick(businessCard: BusinessCard) {
        // Show a confirmation dialog
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this business card?")
            .setPositiveButton("Yes") { _, _ ->
                // User clicked Yes, proceed with deletion
                deleteFromFirebase(businessCard)
            }
            .setNegativeButton("No") { _, _ ->
                // User clicked No, do nothing
            }
            .show()
    }

    private fun deleteFromFirebase(businessCard: BusinessCard) {
        val reference = businessCard.phone?.let { database.getReference("BusinessCard").child(it) }
        reference?.removeValue()?.addOnSuccessListener {
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: Close the current activity if needed
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
