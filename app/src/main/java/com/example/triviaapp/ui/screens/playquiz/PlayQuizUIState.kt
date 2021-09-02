package com.example.triviaapp.ui.screens.playquiz

import com.example.triviaapp.ui.models.Question

sealed class PlayQuizUIState {
    object LoadingState : PlayQuizUIState()

    sealed class QuizQuestionState : PlayQuizUIState() {

        abstract val questionIndex: Int
        abstract val numOfQuestions: Int
        abstract val question: Question
        abstract val timeLimit: Int
        abstract val timeLeft: Int
        abstract val score: Int

        data class QuizMultiple(
            override val questionIndex: Int,
            override val numOfQuestions: Int,
            override val question: Question.QuestionMultiple,
            override val timeLimit: Int,
            override val timeLeft: Int,
            override val score: Int,
            val selectedAnswer: String? = null
        ) : QuizQuestionState()

        data class QuizBoolean(
            override val questionIndex: Int,
            override val numOfQuestions: Int,
            override val question: Question.QuestionBoolean,
            override val timeLimit: Int,
            override val timeLeft: Int,
            override val score: Int,
            val selectedAnswer: Boolean? = null
        ) : QuizQuestionState()
    }
}