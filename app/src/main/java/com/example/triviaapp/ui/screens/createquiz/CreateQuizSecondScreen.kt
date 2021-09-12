package com.example.triviaapp.ui.screens.createquiz

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triviaapp.R
import kotlinx.coroutines.flow.collect
import java.io.FileDescriptor

@Composable
fun CreateQuizSecondScreen(
    viewModel: CreateQuizSecondScreenViewModel,
    onNavigateToPlayQuiz: (Int, Int, Int, Int) -> Unit
) {
    val state by viewModel.stateFlow.collectAsState(CreateQuizSecondScreenState())
    val isSaveAsTemplateEnabled by viewModel.isSaveAsTemplateEnabledFlow.collectAsState(false)
    val saveTemplateDialogState by viewModel.saveTemplateDialogStateFlow.collectAsState(SaveQuizTemplateDialogState.Hidden)
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.showSavedTemplateEventFlow
            .collect {
                Toast.makeText(
                    context,
                    R.string.quiz_template_saved,
                    Toast.LENGTH_LONG
                )
            }
    }
    CreateQuizSecondScreen(
        numOfQuestions = state.numOfQuestions,
        onNumOfQuestionsChanged = { num -> viewModel.updateNumOfQuestions(num) },
        minNumOfQuestions = state.minNumOfQuestions,
        maxNumOfQuestions = state.maxNumOfQuestions,
        timeLimit = state.timeLimit,
        onTimeLimitChanged = { num -> viewModel.updateTimeLimit(num) },
        minTimeLimit = state.minTimeLimit,
        maxTimeLimit = state.maxTimeLimit,
        onPlayClick = {
            val isValid = viewModel.onPlayClick()
            if (isValid) {
                onNavigateToPlayQuiz(
                    state.timeLimit ?: 0,
                    state.category?.id ?: 0,
                    state.difficulty?.ordinal?.plus(1) ?: 0,
                    state.numOfQuestions ?: 0
                )
            }
          },
        numQuestionsErrorState = state.numOfQuestionsErrorState,
        timeLimitErrorState = state.timeLimitErrorState,
        onSaveAsTemplate = { viewModel.showSaveTemplateDialog() },
        isSaveAsTemplateEnabled = isSaveAsTemplateEnabled,
        saveAsTemplateDialogState = saveTemplateDialogState,
        onDismissSaveAsTemplate = { viewModel.hideSaveQuizAsTemplate() },
        onSaveTemplate = { viewModel.onSaveQuizTemplate() },
        onTemplateNameChange = { viewModel.updateTemplateName(it) }
    )
}

@Composable
fun CreateQuizSecondScreen(
    numOfQuestions: Int?,
    onNumOfQuestionsChanged: (Int?) -> Unit,
    minNumOfQuestions: Int,
    maxNumOfQuestions: Int,
    timeLimit: Int?,
    onTimeLimitChanged: (Int?) -> Unit,
    minTimeLimit: Int,
    maxTimeLimit: Int,
    onPlayClick: () -> Unit,
    numQuestionsErrorState: FieldErrorState = FieldErrorState.None,
    timeLimitErrorState: FieldErrorState = FieldErrorState.None,
    onSaveAsTemplate: () -> Unit,
    isSaveAsTemplateEnabled: Boolean,
    saveAsTemplateDialogState: SaveQuizTemplateDialogState,
    onDismissSaveAsTemplate: () -> Unit,
    onSaveTemplate: () -> Unit,
    onTemplateNameChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.limit_num_of_questions, minNumOfQuestions, maxNumOfQuestions),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSecondary
        )
        Row {
            Text(
                text = stringResource(R.string.num_of_questions),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            )
            OutlinedTextField(
                value = numOfQuestions?.toString() ?: "",
                onValueChange = { onNumOfQuestionsChanged(it.toIntOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
                modifier = Modifier
                    .scale(scaleX = 1.0f, scaleY = 0.8f)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
        when (numQuestionsErrorState) {
            FieldErrorState.TooLow -> {
                Text(
                    text = stringResource(R.string.error_num_questions_too_low),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.error
                )
            }
            FieldErrorState.TooHigh -> {
                Text(
                    text = stringResource(R.string.error_num_questions_too_high),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.error
                )
            }
            FieldErrorState.Empty -> {
                Text(
                    text = stringResource(R.string.error_num_questions_empty),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.error
                )
            }
            FieldErrorState.None -> {}
        }
        Text(
            text = stringResource(id = R.string.limit_time_limit, minTimeLimit, maxTimeLimit),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSecondary
        )
        Row {
            Text(
                text = stringResource(R.string.time_limit),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            )
            OutlinedTextField(
                value = timeLimit?.toString() ?: "",
                onValueChange = { onTimeLimitChanged(it.toIntOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
                modifier = Modifier
                    .scale(scaleX = 1.0f, scaleY = 0.8f)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
        when (timeLimitErrorState) {
            FieldErrorState.TooLow -> {
                Text(
                    text = stringResource(R.string.error_time_limit_too_low),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.error
                )
            }
            FieldErrorState.TooHigh -> {
                Text(
                    text = stringResource(R.string.error_time_limit_too_high),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.error
                )
            }
            FieldErrorState.Empty -> {
                Text(
                    text = stringResource(R.string.error_time_limit_empty),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.error
                )
            }
            FieldErrorState.None -> {}
        }
        Button(
            onClick = onPlayClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = stringResource(R.string.play))
        }
        Button(
            onClick = onSaveAsTemplate,
            enabled = isSaveAsTemplateEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = stringResource(R.string.save_as_quiz_template))
        }
    }
    if (saveAsTemplateDialogState is SaveQuizTemplateDialogState.Shown) {
        AlertDialog(
            onDismissRequest = onDismissSaveAsTemplate,
            title = {
                Text(
                    text = stringResource(R.string.save_as_quiz_template),
                    style = MaterialTheme.typography.h5
                )
            },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.message_save_template),
                        style = MaterialTheme.typography.body1
                    )
                    OutlinedTextField(
                        value = saveAsTemplateDialogState.name ?: "",
                        onValueChange = onTemplateNameChange
                    )
                    saveAsTemplateDialogState.error?.let { error ->
                        val errorMessage = when (error) {
                            TemplateNameError.Empty -> stringResource(R.string.quiz_template_empty)
                            TemplateNameError.AlreadyExists -> stringResource(R.string.quiz_template_exists)
                        }
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onSaveTemplate) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissSaveAsTemplate) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
@Preview
fun PreviewCreateQuizSecondScreen() {
    CreateQuizSecondScreen(
        numOfQuestions = 7,
        onNumOfQuestionsChanged = {},
        minNumOfQuestions = 5,
        maxNumOfQuestions = 25,
        timeLimit = 15,
        onTimeLimitChanged = {},
        minTimeLimit = 5,
        maxTimeLimit = 60,
        onPlayClick = {},
        onSaveAsTemplate = {},
        isSaveAsTemplateEnabled = true,
        saveAsTemplateDialogState = SaveQuizTemplateDialogState.Hidden,
        onDismissSaveAsTemplate = {},
        onSaveTemplate = {},
        onTemplateNameChange = {}
    )
}