package com.example.triviaapp.ui.screens.createquiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CreateQuizSecondScreen(
    viewModel: CreateQuizSecondScreenViewModel
) {
    val state by viewModel.stateFlow.collectAsState(CreateQuizSecondScreenState())
    CreateQuizSecondScreen(
        numOfQuestions = state.numOfQuestions,
        onNumOfQuestionsChanged = { viewModel.updateNumOfQuestions(it) },
        maxNumOfQuestions = state.maxNumOfQuestions,
        timeLimit = state.timeLimit,
        onTimeLimitChanged = { viewModel.updateTimeLimit(it) }
    )
}

@Composable
fun CreateQuizSecondScreen(
    numOfQuestions: Int,
    onNumOfQuestionsChanged: (Int) -> Unit,
    maxNumOfQuestions: Int,
    timeLimit: Int,
    onTimeLimitChanged: (Int) -> Unit
) {
    Column {
        Text(text = "Max num of questions = $maxNumOfQuestions")
        Row {
            Text(text = "Num of questions:")
            OutlinedTextField(
                value = numOfQuestions.toString(),
                onValueChange = { it.toIntOrNull()?.let { num -> onNumOfQuestionsChanged(num) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row {
            Text(text = "Time limit:")
            OutlinedTextField(
                value = timeLimit.toString(),
                onValueChange = { it.toIntOrNull()?.let { num -> onTimeLimitChanged(num) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Button(onClick = {}) {
            Text(text = "Play")
        }
    }
}