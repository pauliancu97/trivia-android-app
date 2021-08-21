package com.example.triviaapp.ui.dagger.modules

import com.example.triviaapp.ui.network.services.TriviaService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideTriviaService(): TriviaService =
        Retrofit.Builder()
            .baseUrl(TriviaService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TriviaService::class.java)

}