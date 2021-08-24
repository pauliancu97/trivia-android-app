package com.example.triviaapp.ui.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val text: String,
    val questionId: Int,
    val isCorrect: Boolean
)