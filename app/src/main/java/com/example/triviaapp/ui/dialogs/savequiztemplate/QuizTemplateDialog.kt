package com.example.triviaapp.ui.dialogs.savequiztemplate

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.R
import kotlinx.coroutines.launch

@Composable
fun QuizTemplateDialog(
    viewModel: QuizTemplateDialogViewModel,
    onQuizTemplateSaved: (String) -> Unit
) {
    val state by viewModel.stateFlow.collectAsState(QuizTemplateDialogState.Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    QuizTemplateDialog(
        quizTemplateDialogState = state,
        onNameChanged = { name -> viewModel.updateName(name) },
        onCancel = { viewModel.hide() },
        onConfirm = {
            scope.launch {
                viewModel.save()?.let { quizTemplateName ->
                    val snackBarMessageId = when (state) {
                        is QuizTemplateDialogState.Shown.Save -> R.string.saved_template_text
                        is QuizTemplateDialogState.Shown.Edit -> R.string.renamed_quiz_template
                        else -> null
                    }
                    if (snackBarMessageId != null) {
                        onQuizTemplateSaved(
                            context.resources.getString(
                                snackBarMessageId,
                                quizTemplateName
                            )
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun QuizTemplateDialog(
    quizTemplateDialogState: QuizTemplateDialogState,
    onNameChanged: (String) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    if (quizTemplateDialogState is QuizTemplateDialogState.Shown) {
        AlertDialog(
            title = {
                val titleId = when (quizTemplateDialogState) {
                    is QuizTemplateDialogState.Shown.Save -> R.string.save_as_quiz_template
                    is QuizTemplateDialogState.Shown.Edit -> R.string.rename_quiz_template
                }
                Text(
                    text = stringResource(titleId),
                    style = MaterialTheme.typography.h5
                )
            },
            text = {
                Column {
                    val messageId = when (quizTemplateDialogState) {
                        is QuizTemplateDialogState.Shown.Save -> R.string.message_save_template
                        is QuizTemplateDialogState.Shown.Edit -> R.string.rename_quiz_template_message
                    }
                    Text(
                        text = stringResource(messageId),
                        style = MaterialTheme.typography.body1
                    )
                    OutlinedTextField(
                        value = quizTemplateDialogState.name ?: "",
                        onValueChange = onNameChanged
                    )
                    quizTemplateDialogState.error?.let { error ->
                        val errorMessageId = when (error) {
                            QuizTemplateNameError.Empty -> R.string.quiz_template_empty
                            QuizTemplateNameError.AlreadyExists -> R.string.quiz_template_exists
                            QuizTemplateNameError.NotModified -> R.string.quiz_template_not_renamed
                        }
                        Text(
                            text = stringResource(errorMessageId),
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            },
            onDismissRequest = onCancel,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
@Preview
fun PreviewQuizTemplateDialog() {
    QuizTemplateDialog(
        quizTemplateDialogState = QuizTemplateDialogState.Shown.Edit(
            quizTemplateId = 1,
            name = "Quiz Template",
            error = null
        ),
        onNameChanged = {},
        onConfirm = {},
        onCancel = {}
    )
}