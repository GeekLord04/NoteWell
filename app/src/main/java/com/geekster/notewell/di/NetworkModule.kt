package com.geekster.notewell.di

import com.geekster.notewell.api.UserAPI
import com.geekster.notewell.utlis.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent :: class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit() :  Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofit: Retrofit) : UserAPI {
        return retrofit.create(UserAPI::class.java)
    }
}