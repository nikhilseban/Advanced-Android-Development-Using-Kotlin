package com.nick.foodRecipes.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nick.foodRecipes.data.model.local.Recipe

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: Recipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): Recipe {
        val listType = object : TypeToken<Recipe>() {}.type
        return gson.fromJson(data, listType)
    }

}