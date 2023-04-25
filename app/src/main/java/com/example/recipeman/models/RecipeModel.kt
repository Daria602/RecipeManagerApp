package com.example.recipeman.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RecipeModel(
    val recipeId : String,
    val label : String,
    val image : String,
    val sourceURL : String,
    val ingredientLines : ArrayList<String>,
    val calories : String,
    val mealTypes : ArrayList<String>
) : Parcelable