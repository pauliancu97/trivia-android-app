package com.example.triviaapp.ui.screens.playquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.Question
import com.example.triviaapp.ui.repositories.TriviaRepository
import com.example.triviaapp.ui.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


typealias OnQuizFinishedCallback = (Int, Int, Int, Int) -> Unit

@HiltViewModel
class PlayQuizViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository
): ViewModel() {

    private var onQuizFinishedCallback: OnQuizFinishedCallback? = null

    fun setOnQuizFinishedCallback(callback: OnQuizFinishedCallback) {
        if (onQuizFinishedCallback == null) {
            onQuizFinishedCallback = callback
        }
    }

    private val mutableStateFlow: MutableStateFlow<PlayQuizViewModelState> =
        MutableStateFlow(PlayQuizViewModelState())

    val isQuitQuizDialogShownFlow: Flow<Boolean> = mutableStateFlow.asStateFlow().map { it.isWarningDialogShown }

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
                        totalScore = state.totalScore,
                        score = state.score,
                        correctAnswers = state.correctAnswers,
                        selectedAnswer = state.selectedStringAnswer
                    )
                    is Question.QuestionBoolean -> PlayQuizUIState.QuizQuestionState.QuizBoolean(
                        questionIndex = state.currentQuestionIndex,
                        numOfQuestions = state.questions.size,
                        question = currentQuestion,
                        timeLimit = state.timeLimit,
                        timeLeft = state.timeLeft,
                        totalScore = state.totalScore,
                        score = state.score,
                        correctAnswers = state.correctAnswers,
                        selectedAnswer = state.selectedBooleanAnswer
                    )
                }
            }
        }

    fun initialize(timeLimit: Int, categoryId: Int?, difficulty: Difficulty?, numOfQuestions: Int?, shouldFetchQuestions: Boolean) {
        if (!mutableStateFlow.value.isInitialized) {
            mutableStateFlow.value = PlayQuizViewModelState(
                isInitialized = true,
                isLoading = true,
                timeLimit = timeLimit,
                timeLeft = timeLimit
            )
            viewModelScope.launch {
                if (shouldFetchQuestions) {
                    val category = categoryId?.let { triviaRepository.getCategoryById(it) }
                    triviaRepository.deleteAllQuestions()
                    triviaRepository.fetchQuestions(
                        category, difficulty, numOfQuestions ?: 0
                    )
                }
                val questions = triviaRepository.getQuestions().shuffled()
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
                        isLoading = false,
                        totalScore = questions.size * timeLimit
                    )
                }
                initTickerJob()
                onShowResultListener()
            }
        }
    }

    fun onSelectStringAnswer(answer: String) {
        val currentState = mutableStateFlow.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex) as? Question.QuestionMultiple
        val correctAnswer = currentQuestion?.correctAnswer
        val (updatedScore, updatedCorrectAnswers) = if (correctAnswer == answer) {
            (currentState.score + currentState.timeLeft) to (currentState.correctAnswers + 1)
        } else {
            currentState.score to currentState.correctAnswers
        }
        mutableStateFlow.update {
            copy(
                selectedStringAnswer = answer,
                score = updatedScore,
                correctAnswers = updatedCorrectAnswers
            )
        }
    }

    fun onSelectBooleanAnswer(answer: Boolean) {
        val currentState = mutableStateFlow.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex) as? Question.QuestionBoolean
        val correctAnswer = currentQuestion?.correctAnswer
        val (updatedScore, updatedCorrectAnswers) = if (correctAnswer == answer) {
            (currentState.score + currentState.timeLeft) to (currentState.correctAnswers + 1)
        } else {
            currentState.score to currentState.correctAnswers
        }
        mutableStateFlow.update {
            copy(
                selectedBooleanAnswer = answer,
                score = updatedScore,
                correctAnswers = updatedCorrectAnswers
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
                    if (mutableStateFlow.value.currentQuestionIndex + 1 < mutableStateFlow.value.questions.size) {
                        mutableStateFlow.update {
                            copy(
                                currentQuestionIndex = currentState.currentQuestionIndex + 1,
                                timeLeft = currentState.timeLimit,
                                selectedBooleanAnswer = null,
                                selectedStringAnswer = null
                            )
                        }
                    } else if (!mutableStateFlow.value.isFinished) {
                        mutableStateFlow.update { currentState.copy(isFinished = true) }
                        onQuizFinishedCallback?.invoke(
                            currentState.score,
                            currentState.totalScore,
                            currentState.correctAnswers,
                            currentState.questions.size
                        )
                    }
                }
        }
    }

    fun updateQuitQuizDialogShownStatus(visible: Boolean) {
        mutableStateFlow.update { copy(isWarningDialogShown = visible) }
    }

    fun finish() {
        mutableStateFlow.update { copy(isFinished = true) }
    }

    fun uninitialised() {
        mutableStateFlow.update { copy(isInitialized = false) }
    }
}