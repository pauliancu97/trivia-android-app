package com.example.triviaapp.ui.screens.playquiz

import com.example.triviaapp.ui.models.Question

data class PlayQuizViewModelState(
    val isLoading: Boolean = true,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val timeLimit: Int = 0,
    val timeLeft: Int = 0,
    val selectedStringAnswer: String? = null,
    val selectedBooleanAnswer: Boolean? = null,
    val totalScore: Int = 0,
    val score: Int = 0,
    val correctAnswers: Int = 0,
    val isInitialized: Boolean = false,
    val isFinished: Boolean = false,
    val isWarningDialogShown: Boolean = false
) {
    fun shouldShowResult(): Boolean =
        (timeLeft == 0 || selectedStringAnswer != null || selectedBooleanAnswer != null) && !isFinished
}