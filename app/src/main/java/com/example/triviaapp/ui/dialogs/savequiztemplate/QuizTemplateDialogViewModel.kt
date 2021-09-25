package com.example.triviaapp.ui.dialogs.savequiztemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.DifficultyOption
import com.example.triviaapp.ui.models.QuizTemplate
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

@HiltViewModel
class QuizTemplateDialogViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository
) : ViewModel() {
    private val mutableStateFlow: MutableStateFlow<QuizTemplateDialogState> =
        MutableStateFlow(QuizTemplateDialogState.Hidden)

    val stateFlow: Flow<QuizTemplateDialogState> = mutableStateFlow.asStateFlow()

    fun showForSaving(
        categoryId: Int?,
        categoryName: String?,
        difficultyOption: DifficultyOption,
        numOfQuestions: Int,
        timeLimit: Int
    ) {
        mutableStateFlow.update {
            QuizTemplateDialogState.Shown.Save(
                categoryId, categoryName, difficultyOption, numOfQuestions, timeLimit
            )
        }
    }

    fun showForEditing(quizTemplateName: String) {
        viewModelScope.launch {
            val quizTemplateId = triviaRepository.getQuizTemplateIdWithName(quizTemplateName)
            if (quizTemplateId != null) {
                mutableStateFlow.update {
                    QuizTemplateDialogState.Shown.Edit(
                        quizTemplateId = quizTemplateId,
                        name = quizTemplateName
                    )
                }
            }
        }
    }

    fun hide() {
        mutableStateFlow.update { QuizTemplateDialogState.Hidden }
    }

    fun updateName(name: String?) {
        mutableStateFlow.update {
            if (this is QuizTemplateDialogState.Shown) {
                this.updated(name = name, error = null)
            } else {
                this
            }
        }
    }

    suspend fun save(): String? {
        val state = (mutableStateFlow.value as? QuizTemplateDialogState.Shown) ?: return null
        val quizTemplateName = state.name
        return if (quizTemplateName.isNullOrBlank()) {
            mutableStateFlow.update {
                state.updated(
                    name = quizTemplateName,
                    error = QuizTemplateNameError.Empty
                )
            }
            null
        } else {
            when (state) {
                is QuizTemplateDialogState.Shown.Save -> {
                    val isQuizTemplateWithName = withContext(Dispatchers.IO) {
                        triviaRepository.isQuizTemplateWithName(quizTemplateName)
                    }
                    if (isQuizTemplateWithName) {
                        mutableStateFlow.update {
                            state.copy(error = QuizTemplateNameError.AlreadyExists)
                        }
                        null
                    } else {
                        triviaRepository.saveQuizTemplate(
                            QuizTemplate(
                                name = quizTemplateName,
                                categoryId = state.categoryId,
                                categoryName = state.categoryName,
                                difficultyOption = state.difficultyOption,
                                numOfQuestions = state.numOfQuestions,
                                timeLimit = state.timeLimit
                            )
                        )
                        mutableStateFlow.update { QuizTemplateDialogState.Hidden }
                        quizTemplateName
                    }
                }
                is QuizTemplateDialogState.Shown.Edit -> {
                    val quizTemplatesIdsWithName = triviaRepository.getQuizTemplatesIdsWithName(quizTemplateName)
                    when {
                        quizTemplatesIdsWithName.isEmpty() -> {
                            triviaRepository.updateQuizTemplateName(state.quizTemplateId, quizTemplateName)
                            mutableStateFlow.update { QuizTemplateDialogState.Hidden }
                            quizTemplateName
                        }
                        state.quizTemplateId in quizTemplatesIdsWithName -> {
                            mutableStateFlow.update { state.copy(error = QuizTemplateNameError.NotModified) }
                            null
                        }
                        else -> {
                            mutableStateFlow.update { state.copy(error = QuizTemplateNameError.AlreadyExists) }
                            null
                        }
                    }
                }
            }
        }
    }
}
