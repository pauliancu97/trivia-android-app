package com.example.triviaapp.ui.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategorySubQuestionCountResponse(
    @Json(name = "total_question_count") val numOfQuestions: Int,
    @Json(name = "total_easy_question_count") val numOfEasyQuestions: Int,
    @Json(name = "total_medium_question_count") val numOfNormalQuestions: Int,
    @Json(name = "total_hard_question_count") val numOfHardQuestions: Int
)
