package com.example.triviaapp.ui.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.triviaapp.ui.models.Difficulty

@Entity
data class QuestionBooleanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val categoryId: Int,
    val difficulty: Difficulty,
    val correctAnswer: Boolean
)
