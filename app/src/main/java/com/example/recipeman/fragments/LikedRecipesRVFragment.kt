package com.example.recipeman.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipeman.DBHelper
import com.example.recipeman.R
import com.example.recipeman.adapters.LikedCustomAdapter
import com.example.recipeman.databinding.FragmentLikedRecyclerviewBinding
import com.example.recipeman.retrofit.Recipe
import com.example.recipeman.retrofit.RecipeHit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LikedRecipesRVFragment: Fragment() {
    private lateinit var binding: FragmentLikedRecyclerviewBinding
    private lateinit var likedRecipes: ArrayList<RecipeHit>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikedRecyclerviewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        likedRecipes = arrayListOf()
        val adapter = LikedCustomAdapter(likedRecipes) { recipe ->
            onRecipeClicked(recipe)
        }
        binding.recyclerViewLiked.adapter = adapter
        fetchRecipes()
    }

    private fun fetchRecipes() {
        binding.recyclerViewLiked.visibility = View.GONE
        binding.noRecipesYetTV.visibility = View.GONE
        binding.progressBarLiked.visibility = View.VISIBLE
        val dbHelper = DBHelper()
        dbHelper.dbRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val recipeRecord =  postSnapshot.getValue(Recipe::class.java)
                    recipeRecord!!.liked = true
                    Log.d("test", "got here " + recipeRecord.liked)

                    likedRecipes.add(RecipeHit(recipeRecord))
                    val position: Int = if (likedRecipes.size == 0) {
                        0
                    } else {
                        likedRecipes.size - 1
                    }

                    binding.recyclerViewLiked.adapter?.notifyItemInserted(position)

                }
                if (likedRecipes.size == 0) {
                    binding.noRecipesYetTV.visibility = View.VISIBLE

                } else {
                    binding.recyclerViewLiked.visibility = View.VISIBLE
                }
                binding.progressBarLiked.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("test", "not implemented yet")
            }

        })
    }

    private fun onRecipeClicked(recipeHit: RecipeHit) {
        val bundle = Bundle()
        bundle.putString("RECIPE_URI", recipeHit.recipe?.uri)
        bundle.putString("RECIPE_URL", recipeHit.recipe?.url)
        bundle.putString("RECIPE_IMAGE", recipeHit.recipe?.images?.REGULAR?.url)
        bundle.putString("RECIPE_NAME", recipeHit.recipe?.label)
        bundle.putString("RECIPE_CALORIES", recipeHit.recipe?.calories.toString())
        bundle.putParcelableArrayList("RECIPE_INGREDIENTS", recipeHit.recipe?.ingredients)
        val fragment = ViewItemFragment()
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentsContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}