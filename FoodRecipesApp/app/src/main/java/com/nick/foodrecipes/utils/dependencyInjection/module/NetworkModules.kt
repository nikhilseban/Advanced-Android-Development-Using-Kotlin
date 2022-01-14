package com.nick.foodrecipes.utils.dependencyInjection.module

import com.nick.foodrecipes.utils.AppConstants
import com.nick.foodrecipes.utils.networkCalls.RecipeApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModules {

    @Singleton
    @Provides
    fun provideOkHttpClient():OkHttpClient{

        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15,TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConvertFactory():GsonConverterFactory{
        return GsonConverterFactory.create()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient
                        ,gsonConverterFactory: GsonConverterFactory):Retrofit{

        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): RecipeApiServices{

        return retrofit.create(RecipeApiServices::class.java)
    }
}