package com.example.triviaapp.ui.database.entities

import androidx.room.ColumnInfo
import com.example.triviaapp.ui.models.DifficultyOption
import com.example.triviaapp.ui.models.QuizTemplate

data class QuizTemplateResult(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category_name") val categoryName: String?,
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    @ColumnInfo(name = "difficulty") val difficultyOption: DifficultyOptionEntity,
    @ColumnInfo(name = "num_questions") val numOfQuestions: Int,
    @ColumnInfo(name = "time_limit") val timeLimit: Int
) {
    fun toModel() = QuizTemplate(
        name = name,
        categoryName = categoryName,
        categoryId = categoryId,
        difficultyOption = when (difficultyOption) {
            DifficultyOptionEntity.Any -> DifficultyOption.Any
            DifficultyOptionEntity.Easy -> DifficultyOption.Easy
            DifficultyOptionEntity.Medium -> DifficultyOption.Medium
            DifficultyOptionEntity.Hard -> DifficultyOption.Hard
        },
        numOfQuestions = numOfQuestions,
        timeLimit = timeLimit
    )
}