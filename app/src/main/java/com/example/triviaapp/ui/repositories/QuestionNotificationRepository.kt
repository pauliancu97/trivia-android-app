package com.example.triviaapp.ui.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.DifficultyOption
import com.example.triviaapp.ui.models.QuestionType
import com.example.triviaapp.ui.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class QuestionNotificationPeriod(
    val periodValue: Long,
    val periodTimeUnit: TimeUnit
)

class QuestionNotificationRepository @Inject constructor(
    private val triviaRepository: TriviaRepository,
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val KEY_SHARED_PREFERENCES = "KEY_SHARED_PREFERENCES"
        private const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
        private const val KEY_DIFFICULTY = "KEY_DIFFICULTY"
        private const val KEY_PERIOD_VALUE = "KEY_PERIOD_VALUE"
        private const val KEY_PERIOD_TIME_UNIT = "KEY_PERIOD_TIME_UNIT"
        private const val KEY_QUESTION_TYPE = "KEY_QUESTION_TYPE"
        private const val KEY_IS_QUESTION_NOTIFICATION_ENABLED = "KEY_IS_QUESTION_NOTIFICATION_ENABLED"
        private const val DEFAULT_PERIOD_VALUE = 15L
        private val DEFAULT_PERIOD_TIME_UNIT = TimeUnit.MINUTES
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        KEY_SHARED_PREFERENCES,
        Context.MODE_PRIVATE
    )

    suspend fun getCategory(): Category? {
        val categoryId = sharedPreferences.getInt(KEY_CATEGORY_ID, -1)
        return triviaRepository.getCategoryById(categoryId)
    }

    fun setCategory(category: Category?) {
        val categoryId = category?.id ?: -1
        sharedPreferences.edit().putInt(KEY_CATEGORY_ID, categoryId).apply()
    }

    @ExperimentalCoroutinesApi
    fun getCategoryFlow(): Flow<Category?> =
        sharedPreferences.getIntFlow(KEY_CATEGORY_ID)
            .map { categoryId ->
                triviaRepository.getCategoryById(categoryId)
            }

    var difficulty: Difficulty?
        get() {
            val difficultyName = sharedPreferences.getString(KEY_DIFFICULTY, null)
            return difficultyName?.let {
                valueOrNullOf<Difficulty>(it)
            }
        }
        set(value) {
            sharedPreferences.edit().putString(KEY_DIFFICULTY, value?.name ?: "").apply()
        }

    @ExperimentalCoroutinesApi
    fun getDifficultyFlow(): Flow<Difficulty?> =
        sharedPreferences.getStringFlow(KEY_DIFFICULTY)
            .map { valueOrNullOf<Difficulty>(it) }

    private var periodValue: Long
        get() = sharedPreferences.getLong(KEY_PERIOD_VALUE, DEFAULT_PERIOD_VALUE)
        set(value) {
            sharedPreferences.edit().putLong(KEY_PERIOD_VALUE, value).apply()
        }

    private var periodTimeUnit: TimeUnit
        get() {
            val timeUnitName = sharedPreferences.getString(KEY_PERIOD_TIME_UNIT, null)
            return timeUnitName?.let { valueOrNullOf<TimeUnit>(it) } ?: DEFAULT_PERIOD_TIME_UNIT
        }
        set(value) {
            sharedPreferences.edit().putString(KEY_PERIOD_TIME_UNIT, value.name).apply()
        }

    var period: QuestionNotificationPeriod
        get() = QuestionNotificationPeriod(
            periodValue = periodValue,
            periodTimeUnit = periodTimeUnit
        )
        set(value) {
            periodValue = value.periodValue
            periodTimeUnit = value.periodTimeUnit
        }

    @ExperimentalCoroutinesApi
    fun getPeriodFlow(): Flow<QuestionNotificationPeriod> =
        combine(
            sharedPreferences.getLongFlow(KEY_PERIOD_VALUE),
            sharedPreferences.getStringFlow(KEY_PERIOD_TIME_UNIT)
                .map { valueOrNullOf<TimeUnit>(it) ?: DEFAULT_PERIOD_TIME_UNIT }
        ) { periodValue, periodTimeUnit ->
            QuestionNotificationPeriod(periodValue, periodTimeUnit)
        }

    var isQuestionNotificationEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_QUESTION_NOTIFICATION_ENABLED, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_IS_QUESTION_NOTIFICATION_ENABLED, value).apply()
        }

    @ExperimentalCoroutinesApi
    fun getIsQuestionNotificationEnabledFlow(): Flow<Boolean> =
        sharedPreferences.getBooleanFlow(KEY_IS_QUESTION_NOTIFICATION_ENABLED)

    var questionType: QuestionType?
        get() {
            val questionTypeName = sharedPreferences.getString(KEY_QUESTION_TYPE, null)
            return questionTypeName?.let { valueOrNullOf<QuestionType>(it) }
        }
        set(value) {
            sharedPreferences.edit().putString(KEY_QUESTION_TYPE, value?.name ?: "").apply()
        }

    @ExperimentalCoroutinesApi
    fun getQuestionTypeFlow(): Flow<QuestionType?> =
        sharedPreferences.getStringFlow(KEY_QUESTION_TYPE)
            .map { valueOrNullOf<QuestionType>(it) }
}