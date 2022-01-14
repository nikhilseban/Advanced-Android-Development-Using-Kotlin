package com.nick.foodrecipes.data

import com.nick.foodrecipes.data.model.local.FoodRecipe
import com.nick.foodrecipes.data.networkCalls.RecipeApiServices
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipeApiServices: RecipeApiServices
) {

    suspend fun getRecipes(query: Map<String,String>):Response<FoodRecipe>{
       return recipeApiServices.getRecipes(query)
    }
}