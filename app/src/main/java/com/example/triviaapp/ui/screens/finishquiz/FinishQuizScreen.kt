package com.example.triviaapp.ui.screens.finishquiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triviaapp.R

@Composable
fun FinishQuizScreen(
    score: Int,
    totalScore: Int,
    numOfQuestions: Int,
    numOfCorrectAnswers: Int,
    onReplayQuiz: () -> Unit,
    onCreateNewQuiz: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = stringResource(R.string.finished_quiz),
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 200.dp)
        )
        Text(
            text = stringResource(
                R.string.score,
                score,
                totalScore
            ),
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 75.dp)
        )
        Text(
            text = stringResource(
                R.string.correct_answers,
                numOfCorrectAnswers,
                numOfQuestions
            ),
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        Button(
            onClick = onReplayQuiz,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 50.dp)
        ) {
            Text(text = stringResource(R.string.replay_quiz))
        }
        Button(
            onClick = onCreateNewQuiz,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.create_new_quiz)
            )
        }
    }
}

@Composable
@Preview
fun PreviewFinishQuizScreen() {
    FinishQuizScreen(
        score = 30,
        totalScore = 60,
        numOfQuestions = 12,
        numOfCorrectAnswers = 6,
        onReplayQuiz = {},
        onCreateNewQuiz = {}
    )
}