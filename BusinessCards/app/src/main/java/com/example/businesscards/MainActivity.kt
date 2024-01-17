package com.example.businesscards

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.businesscards.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("Users")

        binding.loginButton.setOnClickListener(View.OnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            // Check for empty fields
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            // Check the user credentials in Firebase
            database.orderByChild("name").equalTo(username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (userSnapshot in dataSnapshot.children) {
                                val user = userSnapshot.getValue(User::class.java)
                                if (user != null && user.password == password) {
                                    // Login Successful
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Login Successful!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@MainActivity, ViewDataActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    return
                                }
                            }
                        }

                        // Login Failed
                        Toast.makeText(
                            this@MainActivity,
                            "Invalid username or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(
                            this@MainActivity,
                            "Database error: " + databaseError.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        })

        binding.registerLink.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        })
    }
}
