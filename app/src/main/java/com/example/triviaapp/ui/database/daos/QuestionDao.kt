package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.example.triviaapp.ui.database.entities.QuestionBooleanEntity
import com.example.triviaapp.ui.database.entities.QuestionMultipleEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(question: QuestionMultipleEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insert(question: QuestionBooleanEntity): Long
}