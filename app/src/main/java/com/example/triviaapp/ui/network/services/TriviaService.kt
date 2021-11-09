package com.example.triviaapp.ui.network.services

import com.example.triviaapp.ui.network.models.CategoryLookupResponse
import com.example.triviaapp.ui.network.models.CategoryQuestionCountResponse
import com.example.triviaapp.ui.network.models.QuestionsLookupResponse
import com.example.triviaapp.ui.network.models.TokenResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaService {
    @GET("api_category.php")
    suspend fun getCategoriesLookupResponse(): CategoryLookupResponse

    @GET("api_count.php")
    suspend fun getCategoryQuestionCountResponse(@Query("category") categoryId: Int): CategoryQuestionCountResponse

    @GET("api_token.php?command=request")
    suspend fun getTokenResponse(): TokenResponse

    @GET("api.php")
    suspend fun getQuestionsLookupResponse(
        @Query("difficulty") difficulty: String?,
        @Query("category") category: Int?,
        @Query("amount") numOfQuestions: Int,
        @Query("token") token: String?,
        @Query("encode") encode: String = "urlLegacy"
    ): QuestionsLookupResponse

    @GET("api.php")
    suspend fun getQuestionsLookupResponse(
        @Query("difficulty") difficulty: String?,
        @Query("category") category: Int?,
        @Query("type") type: String?,
        @Query("amount") numOfQuestions: Int,
        @Query("token") token: String?,
        @Query("encode") encode: String = "urlLegacy"
    ): QuestionsLookupResponse

    companion object {
        const val BASE_URL = "https://opentdb.com/"
    }
}