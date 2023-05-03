package com.example.recipeman.retrofit

@kotlinx.serialization.Serializable
class Recipe(
    val uri: String? = null,
    val label: String? = null,
    val images: ImageTypes? = null,
    val source: String? = null,
    val ingredients: ArrayList<Ingredient>? = null,
    val calories: Double? = null
)