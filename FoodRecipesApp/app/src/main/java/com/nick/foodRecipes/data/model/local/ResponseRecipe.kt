package com.nick.foodRecipes.data.model.local

import com.google.gson.annotations.SerializedName

data class ResponseRecipe(
    @SerializedName("results")
    val recipes: List<Recipe>
)