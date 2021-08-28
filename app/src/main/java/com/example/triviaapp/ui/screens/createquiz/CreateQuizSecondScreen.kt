package com.example.triviaapp.ui.screens.createquiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.R
import java.io.FileDescriptor

@Composable
fun CreateQuizSecondScreen(
    viewModel: CreateQuizSecondScreenViewModel
) {
    val state by viewModel.stateFlow.collectAsState(CreateQuizSecondScreenState())
    CreateQuizSecondScreen(
        numOfQuestions = state.numOfQuestions,
        onNumOfQuestionsChanged = { num -> viewModel.updateNumOfQuestions(num) },
        minNumOfQuestions = state.minNumOfQuestions,
        maxNumOfQuestions = state.maxNumOfQuestions,
        timeLimit = state.timeLimit,
        onTimeLimitChanged = { num -> viewModel.updateTimeLimit(num) },
        minTimeLimit = state.minTimeLimit,
        maxTimeLimit = state.maxTimeLimit,
        onPlayClick = { viewModel.onPlayClick() },
        numQuestionsErrorState = state.numOfQuestionsErrorState,
        timeLimitErrorState = state.timeLimitErrorState
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
    timeLimitErrorState: FieldErrorState = FieldErrorState.None
) {
    val focusManager = LocalFocusManager.current
    Column {
        Text(text = stringResource(id = R.string.limit_num_of_questions, minNumOfQuestions, maxNumOfQuestions))
        Row {
            Text(text = stringResource(R.string.num_of_questions))
            OutlinedTextField(
                value = numOfQuestions?.toString() ?: "",
                onValueChange = { onNumOfQuestionsChanged(it.toIntOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) })
            )
        }
        when (numQuestionsErrorState) {
            FieldErrorState.TooLow -> {
                Text(text = stringResource(R.string.error_num_questions_too_low))
            }
            FieldErrorState.TooHigh -> {
                Text(text = stringResource(R.string.error_num_questions_too_high))
            }
            FieldErrorState.Empty -> {
                Text(text = stringResource(R.string.error_num_questions_empty))
            }
            FieldErrorState.None -> {}
        }
        Text(text = stringResource(id = R.string.limit_time_limit, minTimeLimit, maxTimeLimit))
        Row {
            Text(text = stringResource(R.string.time_limit))
            OutlinedTextField(
                value = timeLimit?.toString() ?: "",
                onValueChange = { onTimeLimitChanged(it.toIntOrNull()) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) })
            )
        }
        when (timeLimitErrorState) {
            FieldErrorState.TooLow -> {
                Text(text = stringResource(R.string.error_time_limit_too_low))
            }
            FieldErrorState.TooHigh -> {
                Text(text = stringResource(R.string.error_time_limit_too_high))
            }
            FieldErrorState.Empty -> {
                Text(text = stringResource(R.string.error_time_limit_empty))
            }
            FieldErrorState.None -> {}
        }
        Button(onClick = onPlayClick) {
            Text(text = stringResource(R.string.play))
        }
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
        onPlayClick = {}
    )
}