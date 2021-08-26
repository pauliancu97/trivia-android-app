package com.example.triviaapp.ui.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "response_code") val responseCode: Int,
    @Json(name = "response_message") val responseMessage: String,
    @Json(name = "token") val token: String
)
