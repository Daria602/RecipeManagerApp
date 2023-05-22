package com.example.recipeman.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeman.R
import com.example.recipeman.fragments.LikedRecipesRVFragment

class SeeLiked : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_liked)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_liked, LikedRecipesRVFragment::class.java, null)
                .commit()
        }
    }
}