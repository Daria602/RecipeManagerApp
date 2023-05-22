package com.example.recipeman.retrofit

import com.google.firebase.database.Exclude
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNames


@kotlinx.serialization.Serializable
class ImageTypes @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("regular", "REGULAR")
    var REGULAR: Image? = null
)