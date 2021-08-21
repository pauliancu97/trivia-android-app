package com.example.triviaapp.ui.network.services

import com.example.triviaapp.ui.network.models.CategoryLookupResponse
import com.example.triviaapp.ui.network.models.CategoryQuestionCountResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaService {
    @GET("api_category.php")
    suspend fun getCategoriesLookupResponse(): CategoryLookupResponse

    @GET("api_count.php")
    suspend fun getCategoryQuestionCountResponse(@Query("category") categoryId: Int): CategoryQuestionCountResponse

    companion object {
        const val BASE_URL = "https://opentdb.com/"
    }
}