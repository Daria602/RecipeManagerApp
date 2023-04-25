package com.example.recipeman.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import com.example.recipeman.fragments.FirstFragment
import com.example.recipeman.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StrictMode
            .setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, FirstFragment::class.java, null)
                .commit()
        }
    }
}