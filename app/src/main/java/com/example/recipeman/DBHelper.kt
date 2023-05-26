package com.example.recipeman

import android.util.Log
import com.example.recipeman.retrofit.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DBHelper {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val dbRef : DatabaseReference

    init {
        val userIdentifier : String = if (firebaseAuth.currentUser != null) {
            firebaseAuth.currentUser!!.email!!.split("@")[0]
        } else {
            // This should never happen, but just in case
            "anonymous"
        }
        dbRef = FirebaseDatabase.getInstance().getReference(userIdentifier)
    }

    fun addRecipe(recipe: Recipe) {
        val recipeId = getRecipeIdFromURI(recipe)
        dbRef.child(recipeId).setValue(recipe)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Test", "inserted completely succ")
                } else {
                    Log.d("Test", it.exception.toString())
                }
            }
    }

    fun removeRecipe(recipe: Recipe) {
        val recipeId = getRecipeIdFromURI(recipe)
        val dbRefChild = dbRef.child(recipeId)
        dbRefChild.removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("Test", "removed completely succ")
            } else {
                Log.d("Test", it.exception.toString())
            }
        }
    }

    fun getRecipeIdFromURI(recipe: Recipe): String {
        return recipe.uri!!.split("#recipe_").last()
    }



}
