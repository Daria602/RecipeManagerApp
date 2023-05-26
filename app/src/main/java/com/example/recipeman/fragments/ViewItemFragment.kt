package com.example.recipeman.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.recipeman.RecipeDBModel
import com.example.recipeman.databinding.FragmentViewItemBinding
import com.example.recipeman.retrofit.Ingredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
class ViewItemFragment : Fragment() {
    private lateinit var binding: FragmentViewItemBinding
    private lateinit var tableLayout: TableLayout
    private lateinit var likeButton: LottieAnimationView

    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewItemBinding.inflate(inflater, container, false)
        tableLayout = binding.ingredientTable
        likeButton = binding.animationView

        firebaseAuth = FirebaseAuth.getInstance()
        val userIdentifier : String = if (firebaseAuth.currentUser != null) {
            firebaseAuth.currentUser!!.email!!.split("@")[0]
        } else {
            // This should never happen, but just in case
            "anonymous"
        }
        dbRef = FirebaseDatabase.getInstance().getReference(userIdentifier)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            //Add name
            binding.recipeLabel.text = bundle.getString("RECIPE_NAME")
            // Add picture
            Picasso.get()
                .load(bundle.getString("RECIPE_IMAGE"))
                .resize(300,300)
                .centerCrop()
                .into(binding.recipePicture)
            // Add calories
            val calories = bundle.getString("RECIPE_CALORIES")?.toDouble()?.toInt().toString()
            binding.recipeCalories.text = "Calories: $calories kcal"
            // Add ingredients
            val ingredientListBundle = bundle.getParcelableArrayList("RECIPE_INGREDIENTS", Ingredient::class.java)
            if (ingredientListBundle != null) {
                for (ingredient in ingredientListBundle) {
                    tableLayout.addView(createIngredientRow(ingredient))
                }
            }

            likeButton.setOnClickListener {
                addRecipeToLiked(
                    bundle.getString("RECIPE_URI"),
                    bundle.getString("RECIPE_NAME"),
                    bundle.getString("RECIPE_IMAGE"),
                    bundle.getParcelableArrayList("RECIPE_INGREDIENTS", Ingredient::class.java)
                )
            }
            binding.shareRecipeButton.setOnClickListener {
                shareRecipe(bundle.getString("RECIPE_URL"))
            }

        }
    }

    private fun shareRecipe(recipeUrl: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, recipeUrl)
        intent.putExtra(Intent.EXTRA_TITLE, "Share to:")
        startActivity(Intent.createChooser(intent, null))

    }

    private fun addRecipeToLiked(uri: String?, label: String?, image: String?, ingredientlist: ArrayList<Ingredient>?) {
        if (uri == null || label == null || image == null) {
            return
        }
        val recipeIdAPI = uri.split("#recipe_").last()

        val recipeId = dbRef.push().key!!

        val recipe = RecipeDBModel(recipeId, recipeIdAPI, label, image, ingredientlist)

        dbRef.child(recipeId).setValue(recipe)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Test", "inserted succ")
                } else {
                    Log.d("Test", it.exception.toString())
                }
            }

    }

    private fun createIngredientRow(ingredient: Ingredient) : TableRow {
        val ingredientTableRow = TableRow(context)
        val tvIngredientName = TextView(context)
        val tvIngredientQuantity = TextView(context)
        val tvIngredientMeasure = TextView(context)

        tvIngredientName.width = 0
        tvIngredientQuantity.width = 0
        tvIngredientMeasure.width = 0

        tvIngredientName.text = ingredient.food.toString()
        tvIngredientQuantity.text = ingredient.quantity.toString()
        if (ingredient.measure == "<unit>") {
            tvIngredientMeasure.text = ""
        } else {
            tvIngredientMeasure.text = ingredient.measure
        }

        tvIngredientName.setPadding(10,10,10,10)
        tvIngredientQuantity.setPadding(10,10,10,10)
        tvIngredientMeasure.setPadding(10,10,10,10)

        tvIngredientName.textSize = 20f
        tvIngredientQuantity.textSize = 20f
        tvIngredientMeasure.textSize = 20f



        ingredientTableRow.addView(tvIngredientName)
        ingredientTableRow.addView(tvIngredientQuantity)
        ingredientTableRow.addView(tvIngredientMeasure)

        return ingredientTableRow
    }
}