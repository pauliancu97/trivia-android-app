package com.example.triviaapp.ui.dialogs.playquiztemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class PlayQuizTemplateDialogViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository
) : ViewModel() {

    private val mutableStateFlow: MutableStateFlow<PlayQuizTemplateDialogState> =
        MutableStateFlow(PlayQuizTemplateDialogState.Hidden)

    val stateFlow: Flow<PlayQuizTemplateDialogState> = mutableStateFlow.asStateFlow()

    fun show(quizTemplateName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { triviaRepository.getQuizTemplateWithName(quizTemplateName) }
                ?.let { quizTemplate ->
                    mutableStateFlow.update { PlayQuizTemplateDialogState.Shown(quizTemplate) }
                }
        }
    }

    fun hide() {
        mutableStateFlow.update { PlayQuizTemplateDialogState.Hidden }
    }
}