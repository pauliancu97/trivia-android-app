package com.example.triviaapp.ui.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.triviaapp.R
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.QuestionType
import com.example.triviaapp.ui.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class QuestionNotificationPeriod(
    val periodValue: Long,
    val periodTimeUnit: TimeUnit
)

enum class QuestionNotificationPeriodOption(
    val textId: Int,
    val period: QuestionNotificationPeriod
) {
    FifteenMinutes(
        R.string.fifteen_minutes,
        QuestionNotificationPeriod(
            15L,
            TimeUnit.MINUTES
        )
    ),
    ThirtyMinutes(
        R.string.thirty_minutes,
        QuestionNotificationPeriod(
            30L,
            TimeUnit.MINUTES
        )
    ),
    OneHour(
        R.string.one_hour,
        QuestionNotificationPeriod(
            1L,
            TimeUnit.HOURS
        )
    ),
    TwoHours(
        R.string.two_hours,
        QuestionNotificationPeriod(
            2L,
            TimeUnit.HOURS
        )
    )
}

class QuestionNotificationRepository @Inject constructor(
    private val triviaRepository: TriviaRepository,
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val KEY_SHARED_PREFERENCES = "KEY_SHARED_PREFERENCES"
        private const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
        private const val KEY_DIFFICULTY = "KEY_DIFFICULTY"
        private const val KEY_PERIOD_OPTION = "KEY_PERIOD_OPTION"
        private const val KEY_QUESTION_TYPE = "KEY_QUESTION_TYPE"
        private const val KEY_IS_QUESTION_NOTIFICATION_ENABLED = "KEY_IS_QUESTION_NOTIFICATION_ENABLED"
        private val DEFAULT_PERIOD_OPTION = QuestionNotificationPeriodOption.FifteenMinutes.name
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

    var periodOption: QuestionNotificationPeriodOption
        get() = sharedPreferences
            .getString(KEY_PERIOD_OPTION, DEFAULT_PERIOD_OPTION)
            ?.let { valueOrNullOf<QuestionNotificationPeriodOption>(it) }
            ?: QuestionNotificationPeriodOption.FifteenMinutes
        set(value) {
            sharedPreferences
                .edit()
                .putString(KEY_PERIOD_OPTION, value.name)
                .apply()
        }

    var isQuestionNotificationEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_QUESTION_NOTIFICATION_ENABLED, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_IS_QUESTION_NOTIFICATION_ENABLED, value).apply()
        }

    var questionType: QuestionType?
        get() {
            val questionTypeName = sharedPreferences.getString(KEY_QUESTION_TYPE, null)
            return questionTypeName?.let { valueOrNullOf<QuestionType>(it) }
        }
        set(value) {
            sharedPreferences.edit().putString(KEY_QUESTION_TYPE, value?.name ?: "").apply()
        }

    fun initialize() {
        if (!sharedPreferences.contains(KEY_IS_QUESTION_NOTIFICATION_ENABLED)) {
            setCategory(null)
            difficulty = null
            periodOption = QuestionNotificationPeriodOption.FifteenMinutes
            questionType = null
            isQuestionNotificationEnabled = true
        }
    }
}