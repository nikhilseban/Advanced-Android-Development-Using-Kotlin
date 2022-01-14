package com.nick.foodrecipes.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class DataRepository @Inject constructor(
    val remoteDataSource: RemoteDataSource
) {
}