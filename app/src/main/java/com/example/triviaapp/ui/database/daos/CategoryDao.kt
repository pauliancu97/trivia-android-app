package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.models.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME} WHERE ${CategoryEntity.ID_COLUMN} = :id LIMIT 1")
    suspend fun getById(id: Int): CategoryEntity?

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun getCategories(): List<CategoryEntity>
}