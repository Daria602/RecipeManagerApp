package com.example.recipeman

import com.example.recipeman.retrofit.Ingredient

data class RecipeDBModel(
    val recipeId: String? = null,
    val uri: String? = null,
    val label: String? = null,
    val image: String? = null,
    val ingredients: ArrayList<Ingredient>? = null
)