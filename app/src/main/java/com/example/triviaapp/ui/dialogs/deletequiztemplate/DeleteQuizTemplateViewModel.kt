package com.example.triviaapp.ui.dialogs.deletequiztemplate

import androidx.lifecycle.ViewModel
import com.example.triviaapp.ui.repositories.TriviaRepository
import com.example.triviaapp.ui.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DeleteQuizTemplateViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository
) : ViewModel() {

    private val mutableStateFlow: MutableStateFlow<DeleteQuizTemplateState> =
        MutableStateFlow(DeleteQuizTemplateState.Hidden)

    val stateFlow: Flow<DeleteQuizTemplateState> = mutableStateFlow.asStateFlow()

    fun show(quizTemplateName: String) {
        mutableStateFlow.update { DeleteQuizTemplateState.Shown(quizTemplateName) }
    }

    fun hide() {
        mutableStateFlow.update { DeleteQuizTemplateState.Hidden }
    }

    suspend fun deleteQuizTemplate(): String? {
        val quizTemplateName = (mutableStateFlow.value as? DeleteQuizTemplateState.Shown)?.quizTemplateName
        if (quizTemplateName != null) {
            withContext(Dispatchers.IO) { triviaRepository.deleteQuizTemplateWithName(quizTemplateName) }
        }
        mutableStateFlow.update { DeleteQuizTemplateState.Hidden }
        return quizTemplateName
    }
}