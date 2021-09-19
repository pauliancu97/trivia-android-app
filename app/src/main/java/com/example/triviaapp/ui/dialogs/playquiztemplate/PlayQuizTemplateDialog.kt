package com.example.triviaapp.ui.dialogs.playquiztemplate

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.R
import com.example.triviaapp.ui.models.DifficultyOption
import com.example.triviaapp.ui.models.QuizTemplate

@Composable
fun PlayQuizTemplateDialog(
    playQuizTemplateDialogViewModel: PlayQuizTemplateDialogViewModel,
    onNavigateToPlayQuiz: (Int, Int, Int, Int) -> Unit
) {
    val state by playQuizTemplateDialogViewModel.stateFlow.collectAsState(PlayQuizTemplateDialogState.Hidden)
    PlayQuizTemplateDialog(
        playQuizTemplateDialogState = state,
        onPlayClick = { quizTemplate ->
            onNavigateToPlayQuiz(
                quizTemplate.timeLimit,
                quizTemplate.categoryId ?: 0,
                quizTemplate.difficultyOption.ordinal,
                quizTemplate.numOfQuestions
            )
        },
        onCancelClick = { playQuizTemplateDialogViewModel.hide() }
    )
}

@Composable
fun PlayQuizTemplateDialog(
    playQuizTemplateDialogState: PlayQuizTemplateDialogState,
    onPlayClick: (QuizTemplate) -> Unit,
    onCancelClick: () -> Unit
) {
    if (playQuizTemplateDialogState is PlayQuizTemplateDialogState.Shown) {
        val quizTemplate = playQuizTemplateDialogState.quizTemplate
        AlertDialog(
            title = {
                Text(
                    text = stringResource(R.string.play_quiz_title),
                    style = MaterialTheme.typography.h5
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.play_quiz_message,
                        quizTemplate.name
                    )
                )
            },
            onDismissRequest = onCancelClick,
            confirmButton = {
                TextButton(onClick = { onPlayClick(quizTemplate) }) {
                    Text(
                        text = stringResource(R.string.play)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelClick) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }
        )
    }
}

@Composable
@Preview
fun PreviewPlayQuizTemplateDialog() {
    PlayQuizTemplateDialog(
        playQuizTemplateDialogState = PlayQuizTemplateDialogState.Shown(
            QuizTemplate(
                name = "First quiz template",
                categoryName = null,
                categoryId = null,
                difficultyOption = DifficultyOption.Medium,
                numOfQuestions = 15,
                timeLimit = 15
            )
        ),
        onPlayClick = {},
        onCancelClick = {}
    )
}