package com.example.businesscards

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.businesscards.databinding.ActivityUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {

            val name = binding.uploadName.text.toString()
            val business = binding.uploadBusiness.text.toString()
            val location = binding.uploadLocation.text.toString()
            val phone = binding.uploadPhone.text.toString()

            database = FirebaseDatabase.getInstance().getReference("BusinessCard")
            val users = BusinessCard(name, business, location, phone)
            database.child(phone).setValue(users).addOnSuccessListener {

                binding.uploadName.text.clear()
                binding.uploadBusiness.text.clear()
                binding.uploadLocation.text.clear()
                binding.uploadPhone.text.clear()

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@UploadActivity, ViewDataActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

        var actionBar = supportActionBar

        // showing the back button in the action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Navigate to ViewDataActivity
                val intent = Intent(this, ViewDataActivity::class.java)
                startActivity(intent)
                finish() // Optional: Close the current activity if needed
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
