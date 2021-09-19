package com.example.triviaapp.ui.screens.quiztemplates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triviaapp.R
import com.example.triviaapp.ui.dialogs.playquiztemplate.PlayQuizTemplateDialog
import com.example.triviaapp.ui.dialogs.playquiztemplate.PlayQuizTemplateDialogViewModel
import com.example.triviaapp.ui.models.DifficultyOption
import com.example.triviaapp.ui.models.QuizTemplate

@Composable
fun QuizTemplatesScreen(
    quizTemplatesViewModel: QuizTemplatesViewModel,
    playQuizTemplateDialogViewModel: PlayQuizTemplateDialogViewModel,
    onNavigateToPlayQuiz: (Int, Int, Int, Int) -> Unit
) {
    val quizTemplates by quizTemplatesViewModel.getQuizTemplatesFlow().collectAsState(emptyList())
    QuizTemplatesScreen(
        quizTemplates = quizTemplates,
        onPlayClick = { quizTemplateName ->
            playQuizTemplateDialogViewModel.show(quizTemplateName)
        },
        onRenameClick = {},
        onDeleteClick = {}
    )
    PlayQuizTemplateDialog(
        playQuizTemplateDialogViewModel,
        onNavigateToPlayQuiz
    )
}

@Composable
fun QuizTemplatesScreen(
    quizTemplates: List<QuizTemplate>,
    onPlayClick: (String) -> Unit,
    onRenameClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    LazyColumn {
        items(quizTemplates) { quizTemplate ->
            QuizTemplateCard(
                quizTemplate = quizTemplate,
                onPlayClick = { onPlayClick(quizTemplate.name) },
                onRenameClick = { onRenameClick(quizTemplate.name) },
                onDeleteClick = { onDeleteClick(quizTemplate.name) }
            )
        }
    }
}

@Composable
fun QuizTemplateCard(
    quizTemplate: QuizTemplate,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(5.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        modifier = Modifier
            .padding(5.dp)
            .padding(5.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(weight = 2.0f)
            ) {
                Text(
                    text = quizTemplate.name,
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = stringResource(
                        R.string.category_with_arg,
                        quizTemplate.categoryName ?: stringResource(R.string.any)
                    )
                )
                Text(
                    text = stringResource(
                        R.string.difficulty_with_arg,
                        stringResource(quizTemplate.difficultyOption.textId)
                    )
                )
                Text(
                    text = stringResource(
                        R.string.num_questions_with_arg,
                        quizTemplate.numOfQuestions
                    )
                )
                Text(
                    text = stringResource(
                        R.string.time_limit_with_arg,
                        quizTemplate.timeLimit
                    )
                )
            }
            Column(
                modifier = Modifier
                    .weight(weight = 1.0f)
            ) {
                Row {
                    IconButton(onClick = onPlayClick) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Play quiz template button"
                        )
                    }
                    IconButton(onClick = onRenameClick) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Rename quiz template button"
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete quiz template button"
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun QuizTemplateCardPreview() {
    QuizTemplateCard(
        quizTemplate = QuizTemplate(
            name = "First Quiz Template",
            categoryId = 1,
            categoryName = "History",
            difficultyOption = DifficultyOption.Medium,
            numOfQuestions = 10,
            timeLimit = 15
        ),
        onRenameClick = {},
        onDeleteClick = {},
        onPlayClick = {}
    )
}