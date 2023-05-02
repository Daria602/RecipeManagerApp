package com.example.recipeman.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recipeman.R
import com.example.recipeman.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.signOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, AccessActivity::class.java))
        }

        binding.searchRecipes.setOnClickListener {
            startActivity(Intent(this, FindRecipes::class.java))
        }

    }
}