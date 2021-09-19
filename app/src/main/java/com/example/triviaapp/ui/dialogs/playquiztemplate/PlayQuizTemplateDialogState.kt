package com.example.triviaapp.ui.dialogs.playquiztemplate

import com.example.triviaapp.ui.models.QuizTemplate

sealed class PlayQuizTemplateDialogState {
    object Hidden : PlayQuizTemplateDialogState()
    data class Shown(val quizTemplate: QuizTemplate) : PlayQuizTemplateDialogState()
}