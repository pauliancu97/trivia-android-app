package com.example.triviaapp.ui.screens.createquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.repositories.TriviaRepository
import com.example.triviaapp.ui.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateQuizSecondScreenState(
    val category: Category? = null,
    val difficulty: Difficulty? = null,
    val maxNumOfQuestions: Int = 0,
    val numOfQuestions: Int = 0,
    val timeLimit: Int = 0
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

    fun updateNumOfQuestions(numOfQuestions: Int) {
        mutableStateFlow.update { copy(numOfQuestions = numOfQuestions) }
    }

    fun updateTimeLimit(timeLimit: Int) {
        mutableStateFlow.update { copy(timeLimit = timeLimit) }
    }
}