package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.triviaapp.ui.database.entities.QuestionAnswerEntity

@Dao
interface AnswerDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(answer: QuestionAnswerEntity)

    @Query("SELECT * FROM ${QuestionAnswerEntity.TABLE_NAME} WHERE ${QuestionAnswerEntity.QUESTION_ID} = :questionId")
    suspend fun getAnswersForQuestionWithId(questionId: Int): List<QuestionAnswerEntity>
}