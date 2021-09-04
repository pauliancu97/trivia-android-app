package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("DELETE FROM ${QuestionBooleanEntity.TABLE_NAME}")
    suspend fun deleteAllQuestionsBoolean()

    @Query("DELETE FROM ${QuestionMultipleEntity.TABLE_NAME}")
    suspend fun deleteAllQuestionsMultiple()

    @Transaction
    suspend fun deleteAllQuestions() {
        deleteAllQuestionsMultiple()
        deleteAllQuestionsBoolean()
    }
}