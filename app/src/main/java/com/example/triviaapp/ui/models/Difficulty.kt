package com.example.triviaapp.ui.models

import com.example.triviaapp.R

enum class Difficulty(val id: Int, val textId: Int) {
    Easy(0, R.string.easy),
    Medium(1, R.string.medium),
    Hard(2, R.string.hard);

    fun apiString() = when (this) {
        Easy -> "easy"
        Medium -> "medium"
        Hard -> "hard"
    }

    companion object {
        fun fromString(string: String) =
            when (string) {
                "easy" -> Easy
                "medium" -> Medium
                "hard" -> Hard
                else -> null
            }
    }
}

fun Difficulty?.toOption() = when (this) {
    Difficulty.Easy -> DifficultyOption.Easy
    Difficulty.Medium -> DifficultyOption.Medium
    Difficulty.Hard -> DifficultyOption.Hard
    null -> DifficultyOption.Any
}