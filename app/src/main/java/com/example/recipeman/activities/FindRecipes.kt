package com.example.recipeman.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recipeman.fragments.RecyclerViewFragment
import com.example.recipeman.R

class FindRecipes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_recipes)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, RecyclerViewFragment::class.java, null)
                .commit()
        }
    }
}