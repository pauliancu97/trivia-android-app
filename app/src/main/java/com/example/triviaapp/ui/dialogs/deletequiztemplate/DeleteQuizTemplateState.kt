package com.example.triviaapp.ui.dialogs.deletequiztemplate

sealed class DeleteQuizTemplateState {
    object Hidden : DeleteQuizTemplateState()
    data class Shown(val quizTemplateName: String) : DeleteQuizTemplateState()
}