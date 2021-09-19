package com.example.triviaapp.ui.screens.quiztemplates

import androidx.lifecycle.ViewModel
import com.example.triviaapp.ui.repositories.TriviaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizTemplatesViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository
): ViewModel() {
    fun getQuizTemplatesFlow() = triviaRepository.getQuizTemplatesFlow()
}