package com.example.triviaapp.ui.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.triviaapp.ui.database.daos.CategoryDao
import com.example.triviaapp.ui.database.entities.CategoryEntity

@Database(entities = [CategoryEntity::class], version = 1)
abstract class TriviaDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val DATABASE_NAME = "trivia_database"
    }
}