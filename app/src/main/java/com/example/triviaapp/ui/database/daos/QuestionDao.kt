package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.triviaapp.ui.database.entities.QuestionBooleanEntity
import com.example.triviaapp.ui.database.entities.QuestionMultipleEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(question: QuestionMultipleEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insert(question: QuestionBooleanEntity): Long

    @Query("SELECT * FROM ${QuestionMultipleEntity.TABLE_NAME}")
    suspend fun getAllQuestionsMultiple(): List<QuestionMultipleEntity>

    @Query("SELECT * FROM ${QuestionBooleanEntity.TABLE_NAME}")
    suspend fun getAllQuestionBoolean(): List<QuestionBooleanEntity>
}