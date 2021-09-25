package com.example.triviaapp.ui.dialogs.deletequiztemplate

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.triviaapp.R
import kotlinx.coroutines.launch

@Composable
fun DeleteQuizTemplateDialog(
    viewModel: DeleteQuizTemplateViewModel,
    onDeleteQuizTemplate: (String) -> Unit
) {
    val state by viewModel.stateFlow.collectAsState(DeleteQuizTemplateState.Hidden)
    val scope = rememberCoroutineScope()
    val resources = LocalContext.current.resources
    DeleteQuizTemplateDialog(
        deleteQuizTemplateState = state,
        onDelete = {
            scope.launch {
                val quizTemplateName =  viewModel.deleteQuizTemplate()
                if (quizTemplateName != null) {
                    onDeleteQuizTemplate(
                        resources.getString(
                            R.string.deleted_quiz_template,
                            quizTemplateName
                        )
                    )
                }
            }
        },
        onDismiss = {
            viewModel.hide()
        }
    )
}

@Composable
fun DeleteQuizTemplateDialog(
    deleteQuizTemplateState: DeleteQuizTemplateState,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    if (deleteQuizTemplateState is DeleteQuizTemplateState.Shown) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(R.string.delete_quiz_template_title),
                    style = MaterialTheme.typography.h5
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.delete_quiz_template_message,
                        deleteQuizTemplateState.quizTemplateName
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = onDelete) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.no))
                }
            }
        )
    }
}