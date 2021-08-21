package com.example.triviaapp.ui.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryResponse(
    val id: Int,
    val name: String
)
