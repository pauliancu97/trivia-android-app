package com.example.triviaapp.ui.models

import com.example.triviaapp.R

enum class DifficultyOption(val textId: Int) {
    Any(R.string.any),
    Easy(R.string.easy),
    Medium(R.string.medium),
    Hard(R.string.hard);

    fun toDifficulty() = when (this) {
        Easy -> Difficulty.Easy
        Medium -> Difficulty.Medium
        Hard -> Difficulty.Hard
        else -> null
    }
}