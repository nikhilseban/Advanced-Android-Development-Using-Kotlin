package com.nick.foodRecipes.view.fragments.recipes

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.nick.foodRecipes.data.DataRepository
import com.nick.foodRecipes.data.DataStoreRepository
import com.nick.foodRecipes.data.database.RecipesEntity
import com.nick.foodRecipes.data.model.local.ResponseRecipe
import com.nick.foodRecipes.utils.AppConstants.Companion.API_KEY
import com.nick.foodRecipes.utils.AppConstants.Companion.DEFAULT_DIET_TYPE
import com.nick.foodRecipes.utils.AppConstants.Companion.DEFAULT_MEAL_TYPE
import com.nick.foodRecipes.utils.AppConstants.Companion.DEFAULT_RECIPES_NUMBER
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel  @Inject constructor(
    private val repository: DataRepository,
    private val mApplication: Application,
    private val dataStoreRepository: DataStoreRepository

) : AndroidViewModel(mApplication) {

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

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
        var mealType = DEFAULT_MEAL_TYPE
        var dietType = DEFAULT_DIET_TYPE

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }




    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}