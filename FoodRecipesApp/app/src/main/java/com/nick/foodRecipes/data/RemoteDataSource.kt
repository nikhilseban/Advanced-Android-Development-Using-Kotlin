package com.nick.foodRecipes.data

import com.nick.foodRecipes.data.model.local.ResponseRecipe
import com.nick.foodRecipes.data.network.RecipeApiServices
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipeApiServices: RecipeApiServices
) {

    suspend fun getRecipes(query: Map<String,String>):Response<ResponseRecipe>{
       return recipeApiServices.getRecipes(query)
    }
}