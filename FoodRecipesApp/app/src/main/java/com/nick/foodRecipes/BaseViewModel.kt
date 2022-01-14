package com.nick.foodRecipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nick.foodRecipes.data.DataRepository
import com.nick.foodRecipes.utils.NetworkUtils
import javax.inject.Inject

class BaseViewModel @Inject constructor(
    application: Application,
    val repository: DataRepository) : AndroidViewModel(application) {

        var connection = NetworkUtils.hasNetworkConnection(application)
}