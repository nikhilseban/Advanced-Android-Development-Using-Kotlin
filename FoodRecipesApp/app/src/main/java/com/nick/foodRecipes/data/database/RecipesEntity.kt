package com.nick.foodRecipes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nick.foodRecipes.data.model.local.Recipe
import com.nick.foodRecipes.utils.AppConstants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: Recipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}