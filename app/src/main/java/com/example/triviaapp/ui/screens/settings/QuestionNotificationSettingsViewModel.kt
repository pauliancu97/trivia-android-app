package com.example.triviaapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.QuestionType
import com.example.triviaapp.ui.repositories.QuestionNotificationPeriod
import com.example.triviaapp.ui.repositories.QuestionNotificationPeriodOption
import com.example.triviaapp.ui.repositories.QuestionNotificationRepository
import com.example.triviaapp.ui.repositories.TriviaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestionNotificationSettingsState(
    val category: Category? = null,
    val difficulty: Difficulty? = null,
    val periodOption: QuestionNotificationPeriodOption = QuestionNotificationPeriodOption.FifteenMinutes,
    val questionType: QuestionType? = null,
    val isNotificationEnabled: Boolean = false,
    val categories: List<Category> = emptyList()
)

@HiltViewModel
class QuestionNotificationSettingsViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository,
    private val questionNotificationRepository: QuestionNotificationRepository
) : ViewModel() {

    private val categoriesMenuExpandedMutableFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    val categoriesMenuExpandedFlow: Flow<Boolean> = categoriesMenuExpandedMutableFlow.asStateFlow()

    private val difficultyMenuExpandedMutableFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    val difficultyMenuExpandedFlow: Flow<Boolean> = difficultyMenuExpandedMutableFlow.asStateFlow()

    private val questionTypeMenuExpandedMutableFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    val questionTypeMenuExpandedFlow: Flow<Boolean> = questionTypeMenuExpandedMutableFlow.asStateFlow()

    private val periodMenuExpandedMutableFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    val periodMenuExpandedFlow: Flow<Boolean> = periodMenuExpandedMutableFlow.asStateFlow()

    private val questionNotificationSettingsMutableStateFlow: MutableStateFlow<QuestionNotificationSettingsState?> =
        MutableStateFlow(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (questionNotificationSettingsMutableStateFlow.value == null) {
                questionNotificationSettingsMutableStateFlow.value =
                    QuestionNotificationSettingsState(
                        category = questionNotificationRepository.getCategory(),
                        difficulty = questionNotificationRepository.difficulty,
                        periodOption = questionNotificationRepository.periodOption,
                        questionType = questionNotificationRepository.questionType,
                        isNotificationEnabled = questionNotificationRepository.isQuestionNotificationEnabled,
                        categories = triviaRepository.getCategories()
                    )
            }
        }
    }

    fun getQuestionNotificationSettings(): Flow<QuestionNotificationSettingsState> =
        questionNotificationSettingsMutableStateFlow.asStateFlow().filterNotNull()

    fun setIsNotificationEnabled(isNotificationEnabled: Boolean) {
        questionNotificationRepository.isQuestionNotificationEnabled = isNotificationEnabled
        questionNotificationSettingsMutableStateFlow.update {
            it?.copy(isNotificationEnabled = isNotificationEnabled)
        }
    }

    fun setCategory(category: Category?) {
        questionNotificationRepository.setCategory(category)
        questionNotificationSettingsMutableStateFlow.update {
            it?.copy(category = category)
        }
    }

    fun setDifficulty(difficulty: Difficulty?) {
        questionNotificationRepository.difficulty = difficulty
        questionNotificationSettingsMutableStateFlow.update {
            it?.copy(
                difficulty = difficulty
            )
        }
    }

    fun setQuestionType(questionType: QuestionType?) {
        questionNotificationRepository.questionType = questionType
        questionNotificationSettingsMutableStateFlow.update {
            it?.copy(
                questionType = questionType
            )
        }
    }

    fun setQuestionPeriod(questionNotificationPeriodOption: QuestionNotificationPeriodOption) {
        questionNotificationRepository.periodOption = questionNotificationPeriodOption
        questionNotificationSettingsMutableStateFlow.update {
            it?.copy(
                periodOption = questionNotificationPeriodOption
            )
        }
    }

    fun setCategoriesMenuExpandedState(isExpanded: Boolean) {
        categoriesMenuExpandedMutableFlow.value = isExpanded
    }

    fun setDifficultyMenuExpandedState(isExpanded: Boolean) {
        difficultyMenuExpandedMutableFlow.value = isExpanded
    }

    fun setQuestionTypeMenuExpandedState(isExpanded: Boolean) {
        questionTypeMenuExpandedMutableFlow.value = isExpanded
    }

    fun setPeriodOptionMenuExpandedState(isExpanded: Boolean) {
        periodMenuExpandedMutableFlow.value = isExpanded
    }
}