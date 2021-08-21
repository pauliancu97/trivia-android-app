package com.example.triviaapp.ui.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryQuestionCountResponse(
    @Json(name = "category_id") val categoryId: Int,
    @Json(name = "category_question_count") val questionsCount: CategorySubQuestionCountResponse
)