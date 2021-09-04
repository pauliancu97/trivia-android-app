package com.example.triviaapp.ui.screens.playquiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.R
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.Question

@Composable
fun PlayQuizScreen(
    viewModel: PlayQuizViewModel
) {
    val uiState by viewModel.uiStateFlow.collectAsState(initial = PlayQuizUIState.LoadingState)
    PlayQuizScreen(
        state = uiState,
        onSelectedStringAnswer = { answer -> viewModel.onSelectStringAnswer(answer) },
        onSelectedBooleanAnswer = { answer -> viewModel.onSelectBooleanAnswer(answer) }
    )
}

@Composable
fun PlayQuizScreen(
    state: PlayQuizUIState,
    onSelectedStringAnswer: (String) -> Unit,
    onSelectedBooleanAnswer: (Boolean) -> Unit
) {
    when (state) {
        PlayQuizUIState.LoadingState -> PlayQuizScreenLoading()
        is PlayQuizUIState.QuizQuestionState.QuizMultiple -> PlayQuizShowQuestionMultipleScreen(
            questionState = state,
            onSelectAnswer = onSelectedStringAnswer
        )
        is PlayQuizUIState.QuizQuestionState.QuizBoolean -> PlayQuizShowQuestionBooleanScreen(
            questionState = state,
            onSelectAnswer = onSelectedBooleanAnswer
        )
    }
}

@Composable
fun PlayQuizScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(alignment = Alignment.Center)
        )
    }
}

@Composable
fun PlayQuizShowQuestionScreen(questionState: PlayQuizUIState.QuizQuestionState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                R.string.question_out_of,
                questionState.questionIndex + 1,
                questionState.numOfQuestions
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(
                R.string.score,
                questionState.score,
                questionState.numOfQuestions
            ),
            modifier = Modifier.align(
                alignment = Alignment.CenterHorizontally
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.difficulty),
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(questionState.question.difficulty.textId),
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
            Box(
                modifier = Modifier.weight(1f)
            ) {
                CircularProgressIndicator(
                    progress = questionState.timeLeft.toFloat() / questionState.timeLimit.toFloat(),
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                )
                Text(
                    text = questionState.timeLeft.toString(),
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.category),
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Text(
                    text = questionState.question.category.name,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
        Text(
            text = questionState.question.text,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PlayQuizShowQuestionMultipleScreen(
    questionState: PlayQuizUIState.QuizQuestionState.QuizMultiple,
    onSelectAnswer: (String) -> Unit
) {
    val showResult = questionState.selectedAnswer != null || questionState.timeLeft == 0
    Column {
        PlayQuizShowQuestionScreen(questionState)
        Column(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
            for (answer in questionState.question.answers) {
                Button(
                    onClick = {
                        if (!showResult) {
                            onSelectAnswer(answer)
                        }
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (!showResult) {
                            Color.Blue
                        } else {
                            if (answer == questionState.question.correctAnswer) {
                                Color.Green
                            } else if (answer == questionState.selectedAnswer && answer != questionState.question.correctAnswer) {
                                Color.Red
                            } else {
                                Color.Blue
                            }
                        }
                    )
                ) {
                    Text(text = answer)
                }
            }
        }
    }
}

@Composable
fun PlayQuizShowQuestionBooleanScreen(
    questionState: PlayQuizUIState.QuizQuestionState.QuizBoolean,
    onSelectAnswer: (Boolean) -> Unit
) {
    val showResult = questionState.selectedAnswer != null || questionState.timeLeft == 0
    Column {
        PlayQuizShowQuestionScreen(questionState)
        Column(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
            for ((answer, answerString) in listOf(true to stringResource(R.string.true_label), false to stringResource(R.string.false_label))) {
                Button(
                    onClick = {
                        if (!showResult) {
                            onSelectAnswer(answer)
                        }
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (!showResult) {
                            Color.Blue
                        } else {
                            if (answer == questionState.question.correctAnswer) {
                                Color.Green
                            } else if (answer == questionState.selectedAnswer && answer != questionState.question.correctAnswer) {
                                Color.Red
                            } else {
                                Color.Blue
                            }
                        }
                    )
                ) {
                    Text(text = answerString)
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewPlayQuizShowQuestionBooleanScreen() {
    PlayQuizShowQuestionBooleanScreen(questionState =
    PlayQuizUIState.QuizQuestionState.QuizBoolean(
        questionIndex = 1,
        numOfQuestions = 15,
        question = Question.QuestionBoolean(
            text = "Was Lincoln the 4th president of the US?",
            category = Category(
                id = 1,
                name = "History",
                numOfQuestions = 100,
                numOfEasyQuestions = 12,
                numOfMediumQuestions = 28,
                numOfHardQuestions = 60
            ),
            difficulty = Difficulty.Medium,
            correctAnswer = true
        ),
        timeLimit = 15,
        timeLeft = 8,
        selectedAnswer = null,
        score = 3
    ),
        onSelectAnswer = {}
    )
}

@Composable
@Preview
fun PreviewPlayQuizShowQuestionMultipleScreen() {
    PlayQuizShowQuestionMultipleScreen(questionState =
    PlayQuizUIState.QuizQuestionState.QuizMultiple(
        questionIndex = 1,
        numOfQuestions = 15,
        question = Question.QuestionMultiple(
            text = "Who is the 4th president of the US?",
            category = Category(
                id = 1,
                name = "History",
                numOfQuestions = 100,
                numOfEasyQuestions = 12,
                numOfMediumQuestions = 28,
                numOfHardQuestions = 60
            ),
            difficulty = Difficulty.Medium,
            answers = listOf("Lincoln", "Washigton", "Kenedy", "Nixon"),
            correctAnswer = "Lincoln"
        ),
        timeLimit = 15,
        timeLeft = 8,
        selectedAnswer = null,
        score = 3
    ),
        onSelectAnswer = {}
    )
}

@Composable
@Preview
fun PreviewPlayQuizShowQuestionScreen() {
    PlayQuizShowQuestionScreen(questionState =
        PlayQuizUIState.QuizQuestionState.QuizMultiple(
            questionIndex = 1,
            numOfQuestions = 15,
            question = Question.QuestionMultiple(
                text = "Who is the 4th president of the US?",
                category = Category(
                    id = 1,
                    name = "History",
                    numOfQuestions = 100,
                    numOfEasyQuestions = 12,
                    numOfMediumQuestions = 28,
                    numOfHardQuestions = 60
                ),
                difficulty = Difficulty.Medium,
                answers = listOf("Lincoln", "Washigton", "Kenedy", "Nixon"),
                correctAnswer = "Lincoln"
            ),
            timeLimit = 15,
            timeLeft = 8,
            selectedAnswer = null,
            score = 3
        )
    )
}

@Preview
@Composable
fun PreviewPlayQuizScreenLoading() {
    PlayQuizScreenLoading()
}