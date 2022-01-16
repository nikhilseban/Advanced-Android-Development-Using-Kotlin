package com.nick.foodRecipes

import android.app.Application
import androidx.lifecycle.*
import com.nick.foodRecipes.data.DataRepository
import com.nick.foodRecipes.data.database.RecipesEntity
import com.nick.foodRecipes.data.model.local.ResponseRecipe
import com.nick.foodRecipes.utils.AppConstants.Companion.API_KEY
import com.nick.foodRecipes.utils.AppConstants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.nick.foodRecipes.utils.AppConstants.Companion.QUERY_API_KEY
import com.nick.foodRecipes.utils.AppConstants.Companion.QUERY_DIET
import com.nick.foodRecipes.utils.AppConstants.Companion.QUERY_FILL_INGREDIENTS
import com.nick.foodRecipes.utils.AppConstants.Companion.QUERY_NUMBER
import com.nick.foodRecipes.utils.AppConstants.Companion.QUERY_TYPE
import com.nick.foodRecipes.utils.NetworkResult
import com.nick.foodRecipes.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel  @Inject constructor(
    private val repository: DataRepository,
    private val mApplication: Application
) : AndroidViewModel(mApplication) {

    /** ROOM DATABASE */

    val readRecipes: LiveData<List<RecipesEntity>> = repository.localDataSource.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertRecipes(recipesEntity)
        }

    /** RETROFIT */

    var recipesResponse: MutableLiveData<NetworkResult<ResponseRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (NetworkUtils.hasNetworkConnection(mApplication)) {
            try {
                val response = repository.remoteDataSource.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)
                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: ResponseRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<ResponseRecipe>): NetworkResult<ResponseRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.recipes.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(null,foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

}