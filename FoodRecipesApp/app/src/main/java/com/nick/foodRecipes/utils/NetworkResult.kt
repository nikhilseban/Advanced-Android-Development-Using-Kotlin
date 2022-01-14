package com.nick.foodRecipes.utils

sealed class NetworkResult <T>(
    val data : T? = null,
    val message: String? = null
){
    class Success<T>(message: String? = "Success", data: T):NetworkResult<T>(data,message)
    class Error<T>(message: String? = "Failed", data:T? = null):NetworkResult<T>(data,message)
    class Loading<T>:NetworkResult<T>()
}