package com.example.recipeman.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesService {
    @GET("/api/recipes/v2")
    fun allRecipes(
        @Query("type") type: String,
        @Query("app_id") app_id: String,
        @Query("app_key") app_key: String,
        @Query("q") q: String
    ) : Call<RecipesResult>
}