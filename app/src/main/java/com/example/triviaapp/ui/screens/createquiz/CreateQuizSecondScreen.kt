package com.example.triviaapp.ui.screens.createquiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triviaapp.R
import java.io.FileDescriptor

@Composable
fun CreateQuizSecondScreen(
    viewModel: CreateQuizSecondScreenViewModel,
    onNavigateToPlayQuiz: (Int, Int, Int, Int) -> Unit
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