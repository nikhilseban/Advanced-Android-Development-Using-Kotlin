package com.nick.foodRecipes.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nick.foodRecipes.data.model.local.Recipe
import com.nick.foodRecipes.data.model.local.ResponseRecipe

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: ResponseRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): ResponseRecipe {
        val listType = object : TypeToken<ResponseRecipe>() {}.type
        return gson.fromJson(data, listType)
    }

}