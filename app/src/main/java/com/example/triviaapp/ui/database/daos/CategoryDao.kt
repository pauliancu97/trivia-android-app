package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.models.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME} WHERE ${CategoryEntity.ID_COLUMN} = :id LIMIT 1")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT ${CategoryEntity.ID_COLUMN} FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun getCategoriesIds(): List<Int>

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    fun getCategoriesFlow(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME} WHERE ${CategoryEntity.NAME_COLUMN} = :name")
    suspend fun getCategoryByName(name: String): CategoryEntity?

    @Query("SELECT COUNT(*) FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun getNumOfCategories(): Int

    @Query("DELETE FROM ${CategoryEntity.TABLE_NAME} WHERE ${CategoryEntity.ID_COLUMN} IN (:categoriesIds)")
    suspend fun deleteCategories(categoriesIds: Set<Int>)

    @Query("UPDATE ${CategoryEntity.TABLE_NAME} SET " +
            "${CategoryEntity.NUM_OF_QUESTIONS_COLUMN} = :numQuestions, " +
            "${CategoryEntity.NUM_OF_EASY_QUESTIONS_COLUMN} = :numEasyQuestions, " +
            "${CategoryEntity.NUM_OF_MEDIUM_QUESTIONS_COLUMN} = :numMediumQuestions, " +
            "${CategoryEntity.NUM_OF_HARD_QUESTIONS_COLUMN} = :numHardQuestions " +
            "WHERE ${CategoryEntity.ID_COLUMN} = :categoryId")
    suspend fun updateCategory(
        categoryId: Int,
        numQuestions: Int,
        numEasyQuestions: Int,
        numMediumQuestions: Int,
        numHardQuestions: Int
    )
}