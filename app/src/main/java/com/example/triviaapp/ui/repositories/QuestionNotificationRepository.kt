package com.example.triviaapp.ui.repositories

import com.example.triviaapp.ui.models.DifficultyOption
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class QuestionNotificationPeriod(
    val periodValue: Long,
    val periodTimeUnit: TimeUnit
)

class QuestionNotificationRepository @Inject constructor(
    private val triviaRepository: TriviaRepository
) {

    suspend fun getCategory() = triviaRepository.getCategoryById(-1)

    val difficultyOption = DifficultyOption.Any

    val period = QuestionNotificationPeriod(1L, TimeUnit.MINUTES)

    val isQuestionNotificationEnabled = true
}