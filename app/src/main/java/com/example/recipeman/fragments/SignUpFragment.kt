package com.example.recipeman.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.recipeman.activities.MainActivity
import com.example.recipeman.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment: Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.signUpButton.setOnClickListener {
            val email = binding.signUpEmail.text.toString()
            val password = binding.signUpPassword.text.toString()
            val repeatPassword = binding.signUpRepeatPassword.text.toString()
            if (checkFields(email, password, repeatPassword)) {
                signUpAndStartActivity(email, password)
            }
        }
        return binding.root
    }

    private fun checkFields(email : String, password : String, repeatPassword : String): Boolean {
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText( this.context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != repeatPassword) {
            Toast.makeText(this.context, "Passwords don't match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun signUpAndStartActivity(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this.context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this.context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}