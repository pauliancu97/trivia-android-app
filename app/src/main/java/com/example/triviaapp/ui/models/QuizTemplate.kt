package com.example.triviaapp.ui.models

import com.example.triviaapp.ui.database.entities.DifficultyOptionEntity
import com.example.triviaapp.ui.database.entities.QuizTemplateEntity

data class QuizTemplate(
    val name: String,
    val categoryName: String?,
    val categoryId: Int?,
    val difficultyOption: DifficultyOption,
    val numOfQuestions: Int,
    val timeLimit: Int
) {
    fun toEntity() = QuizTemplateEntity(
        id = 0,
        name = name,
        categoryId = categoryId,
        difficultyOption = when(difficultyOption) {
            DifficultyOption.Any -> DifficultyOptionEntity.Any
            DifficultyOption.Easy -> DifficultyOptionEntity.Easy
            DifficultyOption.Medium -> DifficultyOptionEntity.Medium
            DifficultyOption.Hard -> DifficultyOptionEntity.Hard
        },
        numOfQuestions = numOfQuestions,
        timeLimit = timeLimit
    )
}