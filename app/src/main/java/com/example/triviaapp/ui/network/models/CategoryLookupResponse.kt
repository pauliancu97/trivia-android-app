package com.example.triviaapp.ui.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryLookupResponse(
    @Json(name = "trivia_categories") val categories: List<CategoryResponse>
)
