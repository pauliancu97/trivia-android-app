package com.example.triviaapp.ui.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionsLookupResponse(
    @Json(name = "response_code") val responseCode: Int,
    @Json(name = "results") val results: List<QuestionResponse>
)