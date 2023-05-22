package com.example.recipeman.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.recipeman.DBHelper
import com.example.recipeman.R
import com.example.recipeman.adapters.CustomAdapter
import com.example.recipeman.databinding.FragmentRecyclerviewBinding
import com.example.recipeman.retrofit.RecipeHit
import com.example.recipeman.retrofit.RecipesResult
import com.example.recipeman.retrofit.RecipesService
import com.example.recipeman.utils.APP_ID
import com.example.recipeman.utils.APP_KEY
import com.example.recipeman.utils.BASE_URL
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Callback
import retrofit2.Retrofit


class RecyclerViewFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerviewBinding
    private lateinit var recipeList: ArrayList<RecipeHit>
    private lateinit var searchView: SearchView
    private lateinit var recipeResult : RecipesResult
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecyclerviewBinding.inflate(inflater, container, false)

        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {

                    fetchRecipes(query)
                }
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchView.isSubmitButtonEnabled = newText.isNotEmpty()
                return false
            }

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeList = arrayListOf()
        val adapter = CustomAdapter(recipeList) { recipeModel ->
            onRecipeClicked(recipeModel)
        }

        binding.recyclerView.adapter = adapter

        fetchRecipes("food vegetables meat")
    }

    private fun onRecipeClicked(recipeHit: RecipeHit) {
        val bundle = Bundle()
        bundle.putString("RECIPE_URI", recipeHit.recipe?.uri)
        bundle.putString("RECIPE_IMAGE", recipeHit.recipe?.images?.REGULAR?.url)
        bundle.putString("RECIPE_NAME", recipeHit.recipe?.label)
        bundle.putString("RECIPE_CALORIES", recipeHit.recipe?.calories.toString())
        bundle.putParcelableArrayList("RECIPE_INGREDIENTS", recipeHit.recipe?.ingredients)
        val fragment = ViewItemFragment()
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun fetchRecipes(q: String) {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        val recipesService = retrofit.create(RecipesService::class.java)
        recipeResult = RecipesResult()
        recipesService.allRecipes("public", APP_ID, APP_KEY, q)
            .enqueue(
                object: Callback<RecipesResult> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: retrofit2.Call<RecipesResult>,
                        response: retrofit2.Response<RecipesResult>
                    ) {

                        if (response.isSuccessful) {
                            recipeResult = response.body()!!
                            recipeList.clear()
                            val dbHelper = DBHelper()
                            recipeList.addAll(recipeResult.hits!!)
                            binding.recyclerView.adapter?.notifyDataSetChanged()
                            for ((index, recipe) in recipeList.withIndex()) {
                                val recipeId = dbHelper.getRecipeIdFromURI(recipe.recipe!!)
                                dbHelper.dbRef.child(recipeId).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            recipe.recipe.liked = true
                                            binding.recyclerView.adapter?.notifyItemChanged(index)
                                        }
                                        if (index == recipeList.lastIndex) {
                                            binding.progressBar.visibility = View.GONE
                                            binding.recyclerView.visibility = View.VISIBLE
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                                    }

                                })
                            }

                        }

                    }

                    override fun onFailure(call: retrofit2.Call<RecipesResult>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }

                }
            )
    }

}