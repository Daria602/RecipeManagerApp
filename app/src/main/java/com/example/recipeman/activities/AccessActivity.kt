package com.example.recipeman.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeman.R
import com.example.recipeman.databinding.ActivityAccessBinding
import com.example.recipeman.fragments.LoginFragment
import com.google.firebase.auth.FirebaseAuth

class AccessActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            // Goes through the inter activity splash screen to the MainActivity
            val intent = Intent(this, InterSplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.loginFragmentContainer, LoginFragment::class.java, null)
                    .commit()
            }
        }
    }


}