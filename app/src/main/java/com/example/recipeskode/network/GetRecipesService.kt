package com.example.recipeskode.network

import com.example.recipeskode.models.RecipeList
import retrofit2.Call
import retrofit2.http.GET

interface GetRecipeService {

    @GET("/recipes.json")
    fun getRecipes() : Call<RecipeList>
}