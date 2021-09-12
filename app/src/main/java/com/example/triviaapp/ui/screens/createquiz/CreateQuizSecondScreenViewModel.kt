package com.example.triviaapp.ui.screens.createquiz

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.QuizTemplate
import com.example.triviaapp.ui.models.toOption
import com.example.triviaapp.ui.repositories.TriviaRepository
import com.example.triviaapp.ui.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class FieldErrorState {
    None,
    TooHigh,
    TooLow,
    Empty
}

enum class TemplateNameError {
    Empty,
    AlreadyExists
}

sealed class SaveQuizTemplateDialogState {
    object Hidden : SaveQuizTemplateDialogState()
    data class Shown(
        val name: String? = null,
        val error: TemplateNameError? = null
    ) : SaveQuizTemplateDialogState()
}

data class CreateQuizSecondScreenState(
    val category: Category? = null,
    val difficulty: Difficulty? = null,
    val maxNumOfQuestions: Int = 0,
    val minNumOfQuestions: Int = 5,
    val numOfQuestions: Int? = null,
    val timeLimit: Int? = null,
    val maxTimeLimit: Int = 60,
    val minTimeLimit: Int = 5,
    val numOfQuestionsErrorState: FieldErrorState = FieldErrorState.None,
    val timeLimitErrorState: FieldErrorState = FieldErrorState.None
)

@HiltViewModel
class CreateQuizSecondScreenViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository
): ViewModel() {

    private val mutableStateFlow: MutableStateFlow<CreateQuizSecondScreenState> =
        MutableStateFlow(CreateQuizSecondScreenState())

    val stateFlow: Flow<CreateQuizSecondScreenState> = mutableStateFlow.asStateFlow()

    private val mutableSaveTemplateDialogState: MutableStateFlow<SaveQuizTemplateDialogState> =
        MutableStateFlow(SaveQuizTemplateDialogState.Hidden)

    val saveTemplateDialogStateFlow: Flow<SaveQuizTemplateDialogState> = mutableSaveTemplateDialogState.asStateFlow()

    private val mutableShowSavedTemplateEvent: MutableSharedFlow<Unit> =
        MutableSharedFlow()

    val showSavedTemplateEventFlow: Flow<Unit> = mutableShowSavedTemplateEvent.asSharedFlow()

    val isSaveAsTemplateEnabledFlow: Flow<Boolean> =
        stateFlow
            .map { state ->
                (state.numOfQuestions?.let { it in state.minNumOfQuestions..state.maxNumOfQuestions } ?: false) &&
                        (state.timeLimit?.let { it in state.minTimeLimit..state.maxTimeLimit } ?: false)
            }

    fun initialize(categoryId: Int, difficulty: Difficulty?, maxNumOfQuestions: Int) {
        viewModelScope.launch {
            val category = triviaRepository.getCategoryById(categoryId)
            mutableStateFlow.update {
                copy(
                    category = category,
                    difficulty = difficulty,
                    maxNumOfQuestions = maxNumOfQuestions
                )
            }
        }
    }

    fun updateNumOfQuestions(numOfQuestions: Int?) {
        mutableStateFlow.update {
            val nextErrorState = if (numOfQuestions != null) {
                if (numOfQuestions > maxNumOfQuestions) {
                    FieldErrorState.TooHigh
                } else if (numOfQuestions < minNumOfQuestions) {
                    FieldErrorState.TooLow
                } else {
                    FieldErrorState.None
                }
            } else {
                if (numOfQuestionsErrorState == FieldErrorState.Empty) {
                    FieldErrorState.Empty
                } else {
                    FieldErrorState.None
                }
            }
            copy(numOfQuestions = numOfQuestions, numOfQuestionsErrorState = nextErrorState)
        }
    }

    fun updateTimeLimit(timeLimit: Int?) {
        mutableStateFlow.update {
            val nextErrorState = if (timeLimit != null) {
                if (timeLimit > maxTimeLimit) {
                    FieldErrorState.TooHigh
                } else if (timeLimit < minTimeLimit) {
                    FieldErrorState.TooLow
                } else {
                    FieldErrorState.None
                }
            } else {
                if (timeLimitErrorState == FieldErrorState.Empty) {
                    FieldErrorState.Empty
                } else {
                    FieldErrorState.None
                }
            }
            copy(timeLimit = timeLimit, timeLimitErrorState = nextErrorState)
        }
    }

    fun onPlayClick(): Boolean {
        mutableStateFlow.update {
            copy(
                numOfQuestionsErrorState = if (numOfQuestions == null) FieldErrorState.Empty else numOfQuestionsErrorState,
                timeLimitErrorState = if (timeLimit == null) FieldErrorState.Empty else timeLimitErrorState
            )
        }
        return mutableStateFlow.value.numOfQuestionsErrorState == FieldErrorState.None &&
                mutableStateFlow.value.timeLimitErrorState == FieldErrorState.None
    }

    fun showSaveTemplateDialog() {
        mutableSaveTemplateDialogState.value = SaveQuizTemplateDialogState.Shown()
    }

    fun updateTemplateName(templateName: String) {
        mutableSaveTemplateDialogState.update {
            if (this is SaveQuizTemplateDialogState.Shown) {
                copy(name = templateName, error = null)
            } else {
                this
            }
        }
    }

    fun onSaveQuizTemplate() {
        viewModelScope.launch {
            val state = (mutableSaveTemplateDialogState.value as? SaveQuizTemplateDialogState.Shown) ?: return@launch
            if (state.name.isNullOrBlank()) {
                mutableSaveTemplateDialogState.update {
                    state.copy(error = TemplateNameError.Empty)
                }
            } else if (withContext(Dispatchers.IO) { triviaRepository.isQuizTemplateWithName(state.name) }) {
                mutableSaveTemplateDialogState.update {
                    state.copy(error = TemplateNameError.AlreadyExists)
                }
            } else {
                withContext(Dispatchers.IO) {
                    triviaRepository.saveQuizTemplate(
                        QuizTemplate(
                            name = state.name,
                            categoryName = mutableStateFlow.value.category?.name,
                            categoryId = mutableStateFlow.value.category?.id,
                            difficultyOption = mutableStateFlow.value.difficulty.toOption(),
                            numOfQuestions = mutableStateFlow.value.numOfQuestions ?: 0,
                            timeLimit = mutableStateFlow.value.timeLimit ?: 0
                        )
                    )
                }
                mutableSaveTemplateDialogState.update { SaveQuizTemplateDialogState.Hidden }
                mutableShowSavedTemplateEvent.emit(Unit)
            }
        }
    }

    fun hideSaveQuizAsTemplate() {
        mutableSaveTemplateDialogState.update { SaveQuizTemplateDialogState.Hidden }
    }
}