package com.example.recipeman.retrofit

@kotlinx.serialization.Serializable
class RecipesResult(
    val hits: ArrayList<RecipeHit>? = null
)