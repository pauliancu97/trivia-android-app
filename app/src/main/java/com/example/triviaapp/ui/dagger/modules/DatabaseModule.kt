package com.example.triviaapp.ui.dagger.modules

import android.content.Context
import androidx.room.Room
import com.example.triviaapp.ui.database.TriviaDatabase
import com.example.triviaapp.ui.database.daos.CategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideCategoryDao(triviaDatabase: TriviaDatabase): CategoryDao =
        triviaDatabase.categoryDao()

    @Provides
    @Singleton
    fun provideTriviaDatabase(
        @ApplicationContext context: Context
    ): TriviaDatabase = Room.databaseBuilder(
        context,
        TriviaDatabase::class.java,
        TriviaDatabase.DATABASE_NAME
    ).build()

}