package com.example.triviaapp.ui.models

import com.example.triviaapp.R

enum class Difficulty(val id: Int, val textId: Int) {
    Easy(0, R.string.easy),
    Medium(1, R.string.medium),
    Hard(2, R.string.hard)
}

