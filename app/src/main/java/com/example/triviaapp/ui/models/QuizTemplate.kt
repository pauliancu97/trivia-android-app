package com.example.triviaapp.ui.models

data class QuizTemplate(
    val name: String,
    val categoryName: String?,
    val categoryId: Int?,
    val difficultyOption: DifficultyOption,
    val numOfQuestions: Int,
    val timeLimit: Int
)