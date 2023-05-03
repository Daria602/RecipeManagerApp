package com.example.recipeman.retrofit

import android.os.Parcelable

@kotlinx.serialization.Serializable
@kotlinx.parcelize.Parcelize
class Ingredient(
    val food: String? = null,
    val measure: String? = null,
    val quantity: Double? = null
) : Parcelable