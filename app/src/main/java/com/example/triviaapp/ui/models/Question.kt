package com.example.triviaapp.ui.models

sealed class Question {

    abstract val text: String
    abstract val category: Category
    abstract val difficulty: Difficulty

    data class QuestionMultiple(
        override val text: String,
        override val category: Category,
        override val difficulty: Difficulty,
        val answers: List<String>,
        val correctAnswer: String
    ) : Question()

    data class QuestionBoolean(
        override val text: String,
        override val category: Category,
        override val difficulty: Difficulty,
        val correctAnswer: Boolean
    ) : Question()
}