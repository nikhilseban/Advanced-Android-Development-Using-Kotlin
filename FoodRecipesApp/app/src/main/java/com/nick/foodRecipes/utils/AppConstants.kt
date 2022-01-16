package com.nick.foodRecipes.utils

class AppConstants {

    companion object{
        const val BASE_URL:String = "https://api.spoonacular.com"
        const val API_KEY:String = "6db5ab9869824c6ab4f65718d9e42dec"

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"

    }
}