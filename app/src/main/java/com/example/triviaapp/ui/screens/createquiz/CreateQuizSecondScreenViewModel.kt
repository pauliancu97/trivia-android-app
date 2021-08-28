package com.example.triviaapp.ui.screens.createquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.repositories.TriviaRepository
import com.example.triviaapp.ui.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class FieldErrorState {
    None,
    TooHigh,
    TooLow,
    Empty
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
}