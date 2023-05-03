package com.example.recipeman.fragments

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
import com.example.recipeman.databinding.FragmentViewItemBinding
import com.example.recipeman.retrofit.Ingredient
import com.squareup.picasso.Picasso
class ViewItemFragment : Fragment() {
    private lateinit var binding: FragmentViewItemBinding
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewItemBinding.inflate(inflater, container, false)
        tableLayout = binding.ingredientTable
        return binding.root
    }

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
            binding.recipeCalories.text = "Calories: " + bundle.getString("RECIPE_CALORIES") + " kcal"
            // Add ingredients
            val ingredientListBundle = bundle.getParcelableArrayList("RECIPE_INGREDIENTS", Ingredient::class.java)
            if (ingredientListBundle != null) {
                for (ingredient in ingredientListBundle) {
                    tableLayout.addView(createIngredientRow(ingredient))
                }
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

        Log.d("test", ingredient.measure.toString())

        tvIngredientName.setPadding(10,10,10,10)
        tvIngredientQuantity.setPadding(10,10,10,10)
        tvIngredientMeasure.setPadding(10,10,10,10)

        tvIngredientName.textSize = 20f
        tvIngredientQuantity.textSize = 20f
        tvIngredientMeasure.textSize = 20f



        ingredientTableRow.addView(tvIngredientName)
        ingredientTableRow.addView(tvIngredientQuantity)
        ingredientTableRow.addView(tvIngredientMeasure)

        return  ingredientTableRow
    }
}