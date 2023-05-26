package com.example.recipeman.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.recipeman.R
import com.example.recipeman.databinding.ActivityMainBinding
import com.example.recipeman.fragments.LikedRecipesRVFragment
import com.example.recipeman.fragments.RecyclerViewFragment
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggleMenu: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    // For authentication purposes
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        drawerLayout = binding.drawerLayout
        val navigationView = binding.navView

        toggleMenu = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggleMenu)
        toggleMenu.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val header = navigationView.getHeaderView(0)

        val userPicture = header.findViewById<CircleImageView>(R.id.userPicture)
        val userName = header.findViewById<TextView>(R.id.userName)
        val userEmail = header.findViewById<TextView>(R.id.userEmail)

        userName.text = auth.currentUser!!.displayName
        userEmail.text = auth.currentUser!!.email
        if (auth.currentUser!!.photoUrl != null) {
            Picasso.get()
                .load(auth.currentUser!!.photoUrl)
                .into(userPicture)
        } else {
            userPicture.setImageResource(R.drawable.blank_profile_picture)
        }


        // When logging in or just entering the app, navigate to home fragment
        navigationView.setCheckedItem(R.id.navSearchRecipes)
        replaceFragment(RecyclerViewFragment(), "Search for recipes")


        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId) {
                R.id.navSearchRecipes -> {
                    replaceFragment(RecyclerViewFragment(), it.title.toString())
                }
                R.id.navLiked -> {
                    replaceFragment(LikedRecipesRVFragment(), it.title.toString())
                }
                R.id.navLogout -> {
                    auth.signOut()
                    startActivity(Intent(this, AccessActivity::class.java))
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleMenu.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentsContainer, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }
}