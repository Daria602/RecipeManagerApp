package com.example.recipeman.retrofit


@kotlinx.serialization.Serializable
class ImageTypes(
    val THUMBNAIL: Image? = null,
    val SMALL: Image? = null,
    val REGULAR: Image? = null,
    val LARGE: Image? = null
)