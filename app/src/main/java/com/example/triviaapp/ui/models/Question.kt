package com.example.triviaapp.ui.models

import com.example.triviaapp.R

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

enum class QuestionType(val identifier: String, val textId: Int) {
    Multiple("multiple", R.string.multiple_choices),
    Boolean("boolean", R.string.true_false);

    companion object {
        fun fromString(string: String) =
            when (string) {
                "multiple" -> Multiple
                "boolean" -> Boolean
                else -> null
            }
    }
}