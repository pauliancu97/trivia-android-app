package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.example.triviaapp.ui.database.entities.QuestionAnswerEntity

@Dao
interface AnswerDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(answer: QuestionAnswerEntity)
}