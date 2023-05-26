package com.example.recipeman.retrofit

@kotlinx.serialization.Serializable
class Recipe(
    val uri: String? = null,
    val label: String? = null,
    var images: ImageTypes? = null,
    val source: String? = null,
    val url: String? = null,
    val ingredients: ArrayList<Ingredient>? = null,
    val calories: Double? = null,
    var liked: Boolean? = false
)