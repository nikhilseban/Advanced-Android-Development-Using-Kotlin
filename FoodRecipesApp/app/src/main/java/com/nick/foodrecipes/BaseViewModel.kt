package com.nick.foodrecipes

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import com.nick.foodrecipes.data.DataRepository
import com.nick.foodrecipes.utils.NetworkUtils
import javax.inject.Inject

class BaseViewModel @Inject constructor(
    application: Application,
    val repository: DataRepository) : AndroidViewModel(application) {

        var connection = NetworkUtils.hasNetworkConnection(application)
}