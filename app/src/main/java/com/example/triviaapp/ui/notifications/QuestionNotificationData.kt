package com.example.triviaapp.ui.notifications

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class QuestionNotificationData : Parcelable {

    @Parcelize
    data class QuestionBoolean(val correctAnswer: Boolean, val userAnswer: Boolean) : QuestionNotificationData()

    @Parcelize
    data class QuestionMultiple(val correctAnswer: String) : QuestionNotificationData()
}

sealed class AnswerData {
    data class AnswerBoolean(val isCorrect: Boolean) : AnswerData()
    data class AnswerMultiple(val isCorrect: Boolean, val correctAnswer: String) : AnswerData()
}