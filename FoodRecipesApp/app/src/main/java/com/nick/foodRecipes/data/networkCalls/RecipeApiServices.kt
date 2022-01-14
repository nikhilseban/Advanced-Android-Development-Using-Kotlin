package com.nick.foodRecipes.data.networkCalls

import com.nick.foodRecipes.data.model.local.ResponseRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RecipeApiServices {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<ResponseRecipe>

}