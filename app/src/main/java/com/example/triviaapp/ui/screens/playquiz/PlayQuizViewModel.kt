package com.example.triviaapp.ui.screens.playquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.Question
import com.example.triviaapp.ui.repositories.TriviaRepository
import com.example.triviaapp.ui.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayQuizViewModelFactory @Inject constructor(
    private val triviaRepository: TriviaRepository
) {
    fun create(
        timeLimit: Int,
        categoryId: Int,
        difficulty: Difficulty?,
        numOfQuestions: Int
    ) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PlayQuizViewModel(
                triviaRepository, timeLimit, categoryId, difficulty, numOfQuestions
            ) as T
        }
    }
}

class PlayQuizViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository,
    private val timeLimit: Int,
    private val categoryId: Int,
    private val difficulty: Difficulty?,
    private val numOfQuestions: Int
): ViewModel() {

    private val mutableStateFlow: MutableStateFlow<PlayQuizViewModelState> =
        MutableStateFlow(PlayQuizViewModelState())

    val uiStateFlow: Flow<PlayQuizUIState> = mutableStateFlow.asStateFlow()
        .map { state ->
            if (state.isLoading) {
                PlayQuizUIState.LoadingState
            } else {
                when (val currentQuestion = state.questions[state.currentQuestionIndex]) {
                    is Question.QuestionMultiple -> PlayQuizUIState.QuizQuestionState.QuizMultiple(
                        questionIndex = state.currentQuestionIndex,
                        numOfQuestions = state.questions.size,
                        question = currentQuestion,
                        timeLimit = state.timeLimit,
                        timeLeft = state.timeLeft,
                        score = state.score,
                        selectedAnswer = state.selectedStringAnswer
                    )
                    is Question.QuestionBoolean -> PlayQuizUIState.QuizQuestionState.QuizBoolean(
                        questionIndex = state.currentQuestionIndex,
                        numOfQuestions = state.questions.size,
                        question = currentQuestion,
                        timeLimit = state.timeLimit,
                        timeLeft = state.timeLeft,
                        score = state.score,
                        selectedAnswer = state.selectedBooleanAnswer
                    )
                }
            }
        }

    init {
        mutableStateFlow.value = PlayQuizViewModelState(
            isLoading = true,
            timeLimit = timeLimit,
            timeLeft = timeLimit
        )
        viewModelScope.launch {
            val category = triviaRepository.getCategoryById(categoryId)
            val questions = triviaRepository.fetchQuestions(category, difficulty, numOfQuestions)
            mutableStateFlow.update {
                copy(
                    questions = questions
                        .map { question ->
                            if (question is Question.QuestionMultiple) {
                                question.copy(
                                    answers = question.answers.shuffled()
                                )
                            } else {
                                question
                            }
                        },
                    isLoading = false
                )
            }
            initTickerJob()
            onShowResultListener()
        }
    }

    fun onSelectStringAnswer(answer: String) {
        val currentState = mutableStateFlow.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex) as? Question.QuestionMultiple
        val correctAnswer = currentQuestion?.correctAnswer
        val updatedScore = if (correctAnswer == answer) {
            currentState.score + 1
        } else {
            currentState.score
        }
        mutableStateFlow.update {
            copy(
                selectedStringAnswer = answer,
                score = updatedScore
            )
        }
    }

    fun onSelectBooleanAnswer(answer: Boolean) {
        val currentState = mutableStateFlow.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex) as? Question.QuestionBoolean
        val correctAnswer = currentQuestion?.correctAnswer
        val updatedScore = if (correctAnswer == answer) {
            currentState.score + 1
        } else {
            currentState.score
        }
        mutableStateFlow.update {
            copy(
                selectedBooleanAnswer = answer,
                score = updatedScore
            )
        }
    }

    private fun initTickerJob() {
        viewModelScope.launch {
            mutableStateFlow.asStateFlow()
                .filter { !it.shouldShowResult() }
                .map { it.timeLeft }
                .filter { it > 0 }
                .collect {
                    delay(1000)
                    mutableStateFlow.update { copy(timeLeft = it - 1) }
                }
        }
    }

    private fun onShowResultListener() {
        viewModelScope.launch {
            mutableStateFlow.asStateFlow()
                .filter { it.shouldShowResult() }
                .collect { currentState ->
                    delay(5000)
                    if (currentState.currentQuestionIndex + 1 < currentState.questions.size) {
                        mutableStateFlow.update {
                            currentState.copy(
                                currentQuestionIndex = currentState.currentQuestionIndex + 1,
                                timeLeft = currentState.timeLimit,
                                selectedBooleanAnswer = null,
                                selectedStringAnswer = null
                            )
                        }
                    }
                }
        }
    }
}