package com.example.triviaapp.ui.dagger.modules

import com.example.triviaapp.ui.navigation.NavigationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideNavigationManager() = NavigationManager()
}