package com.example.triviaapp.ui.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionResponse(
    @Json(name = "category") val category: String,
    @Json(name = "difficulty") val difficulty: String,
    @Json(name = "type") val type: String,
    @Json(name = "question") val question: String,
    @Json(name = "correct_answer") val correctAnswer: String,
    @Json(name = "incorrect_answers") val incorrectAnswers: List<String>
)
