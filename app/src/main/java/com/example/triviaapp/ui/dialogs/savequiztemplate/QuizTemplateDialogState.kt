package com.example.triviaapp.ui.dialogs.savequiztemplate

import com.example.triviaapp.ui.models.DifficultyOption

sealed class QuizTemplateDialogState {

    object Hidden : QuizTemplateDialogState()

    sealed class Shown : QuizTemplateDialogState() {
        abstract val name: String?
        abstract val error: QuizTemplateNameError?

        data class Save(
            val categoryId: Int?,
            val categoryName: String?,
            val difficultyOption: DifficultyOption,
            val numOfQuestions: Int,
            val timeLimit: Int,
            override val name: String? = null,
            override val error: QuizTemplateNameError? = null
        ) : Shown()

        data class Edit(
            val quizTemplateId: Int,
            override val name: String? = null,
            override val error: QuizTemplateNameError? = null
        ) : Shown()

        fun updated(
            name: String? = null,
            error: QuizTemplateNameError? = null
        ): Shown =
            when (this) {
                is Save -> copy(name = name, error = error)
                is Edit -> copy(name = name, error = error)
            }
    }
}