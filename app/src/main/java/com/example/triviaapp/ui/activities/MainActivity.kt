package com.example.triviaapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.triviaapp.ui.animations.EnterFromRightAnimation
import com.example.triviaapp.ui.dialogs.playquiztemplate.PlayQuizTemplateDialogViewModel
import com.example.triviaapp.ui.dialogs.savequiztemplate.QuizTemplateDialogViewModel
import com.example.triviaapp.ui.models.DifficultyOption
import com.example.triviaapp.ui.navigation.NavigationDestinations
import com.example.triviaapp.ui.screens.createquiz.CreateQuizFirstScreen
import com.example.triviaapp.ui.screens.createquiz.CreateQuizFirstScreenViewModel
import com.example.triviaapp.ui.screens.createquiz.CreateQuizSecondScreen
import com.example.triviaapp.ui.screens.createquiz.CreateQuizSecondScreenViewModel
import com.example.triviaapp.ui.screens.finishquiz.FinishQuizScreen
import com.example.triviaapp.ui.screens.playquiz.PlayQuizScreen
import com.example.triviaapp.ui.screens.playquiz.PlayQuizViewModel
import com.example.triviaapp.ui.screens.quiztemplates.QuizTemplatesScreen
import com.example.triviaapp.ui.screens.quiztemplates.QuizTemplatesViewModel
import com.example.triviaapp.ui.screens.start.StartScreen
import com.example.triviaapp.ui.screens.start.StartScreenViewModel
import com.example.triviaapp.ui.theme.TriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaApp()
        }
    }
}

@Composable
fun TriviaApp() {
    TriviaAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            TriviaAppNavHost()
        }
    }
}

@Composable
fun TriviaAppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.StartScreen.name
    ) {
        composable(NavigationDestinations.StartScreen.name) {
            val viewModel = hiltViewModel<StartScreenViewModel>()
            StartScreen(
                viewModel = viewModel,
                navigateToCreateQuizFirstPage = {
                    navController.navigate(NavigationDestinations.CreateQuizFirstScreen.name)
                },
                navigateToQuizTemplatesPage = {
                    navController.navigate(NavigationDestinations.QuizTemplates.name)
                }
            )
        }
        composable(NavigationDestinations.CreateQuizFirstScreen.name) {
            val viewModel = hiltViewModel<CreateQuizFirstScreenViewModel>()
            EnterFromRightAnimation {
                CreateQuizFirstScreen(
                    viewModel,
                    onNavigateToCreateQuizSecondScreen = { categoryId, difficulty, numQuestions ->
                        navController.navigate(
                            "${NavigationDestinations.CreateQuizSecondScreen}?categoryId=$categoryId&difficulty=$difficulty&numQuestions=$numQuestions"
                        )
                    }
                )
            }
        }
        composable(
            "${NavigationDestinations.CreateQuizSecondScreen}?categoryId={categoryId}&difficulty={difficulty}&numQuestions={numQuestions}",
            arguments = listOf(
                navArgument(
                    name = "categoryId",
                ) {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(
                    name = "difficulty"
                ) {
                    type = NavType.StringType
                    defaultValue = DifficultyOption.Any.name
                },
                navArgument(
                    name = "numQuestions"
                ) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { navBackStackEntry ->
            val categoryId = navBackStackEntry.arguments?.getInt("categoryId") ?: 0
            val difficulty = navBackStackEntry.arguments?.getString("difficulty")
                ?.let { DifficultyOption.valueOf(it) }?.toDifficulty()
            val maxNumOfQuestions = navBackStackEntry.arguments?.getInt("numQuestions") ?: 0
            val viewModel = hiltViewModel<CreateQuizSecondScreenViewModel>()
            val quizTemplateDialogViewModel = hiltViewModel<QuizTemplateDialogViewModel>()
            viewModel.initialize(categoryId, difficulty, maxNumOfQuestions)
            EnterFromRightAnimation {
                CreateQuizSecondScreen(
                    viewModel = viewModel,
                    quizTemplateDialogViewModel = quizTemplateDialogViewModel,
                    onNavigateToPlayQuiz = { timeLimit, categoryId, difficultyId, numOfQuestions ->
                        navController.navigate(
                            "${NavigationDestinations.PlayQuizScreen.name}/$timeLimit/true?categoryId=$categoryId&difficultyId=$difficultyId&numOfQuestions=$numOfQuestions"
                        )
                    }
                )
            }
        }
        composable(
            route = "${NavigationDestinations.PlayQuizScreen.name}/{timeLimit}/{shouldFetchQuestions}?categoryId={categoryId}&difficultyId={difficultyId}&numOfQuestions={numOfQuestions}",
            arguments = listOf(
                navArgument(
                    name = "timeLimit",
                ) {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(
                    name = "categoryId",
                ) {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(
                    name = "difficultyId",
                ) {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(
                    name = "numOfQuestions",
                ) {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(name = "shouldFetchQuestions") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) {
            val timeLimit = it.arguments?.getInt("timeLimit") ?: 0
            val categoryId = it.arguments?.getInt("categoryId")
            val difficulty = it.arguments?.getInt("difficultyId")?.let { id -> DifficultyOption.values().getOrNull(id) }
            val numOfQuestions = it.arguments?.getInt("numOfQuestions")
            val shouldFetchQuestions = it.arguments?.getBoolean("shouldFetchQuestions", true) ?: true
            val viewModel = hiltViewModel<PlayQuizViewModel>()
            viewModel.initialize(
                timeLimit, categoryId, difficulty?.toDifficulty(), numOfQuestions, shouldFetchQuestions
            )
            EnterFromRightAnimation {
                PlayQuizScreen(
                    viewModel,
                    onNavigateToFinishedQuiz = { score, totalScore, numOfCorrectAnswers, numOfQuestions ->
                        val url = "${NavigationDestinations.FinishedQuizScreen.name}/$score/$totalScore/$numOfCorrectAnswers/$numOfQuestions/$timeLimit"
                        navController.navigate(url) {
                            popUpTo(
                                route = NavigationDestinations.CreateQuizFirstScreen.name
                            ) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHomeScreen = {
                        navController.popBackStack(
                            route = NavigationDestinations.StartScreen.name,
                            inclusive = false
                        )
                    }
                )
            }
        }
        composable(
            route = "${NavigationDestinations.FinishedQuizScreen.name}/{score}/{totalScore}/{numCorrectAnswers}/{numQuestions}/{timeLimit}",
            arguments = listOf(
                navArgument(name = "score") {
                    type = NavType.IntType
                },
                navArgument(name = "totalScore") {
                    type = NavType.IntType
                },
                navArgument(name = "numCorrectAnswers") {
                    type = NavType.IntType
                },
                navArgument(name = "numQuestions") {
                    type = NavType.IntType
                },
                navArgument(name = "timeLimit") {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val score = navBackStackEntry.arguments?.getInt("score", 0) ?: 0
            val totalScore = navBackStackEntry.arguments?.getInt("totalScore", 0) ?: 0
            val numOfCorrectAnswers = navBackStackEntry.arguments?.getInt("numCorrectAnswers", 0) ?: 0
            val numOfQuestions = navBackStackEntry.arguments?.getInt("numQuestions", 0) ?: 0
            val timeLimit = navBackStackEntry.arguments?.getInt("timeLimit", 0) ?: 0
            val playQuizViewModel = hiltViewModel<PlayQuizViewModel>()
            EnterFromRightAnimation {
                FinishQuizScreen(
                    score = score,
                    totalScore = totalScore,
                    numOfQuestions = numOfQuestions,
                    numOfCorrectAnswers = numOfCorrectAnswers,
                    onReplayQuiz = {
                        playQuizViewModel.uninitialised()
                        navController.navigate(
                            "${NavigationDestinations.PlayQuizScreen.name}/$timeLimit/false"
                        ) {
                            popUpTo(
                                route = "${NavigationDestinations.FinishedQuizScreen.name}/{score}/{totalScore}/{numCorrectAnswers}/{numQuestions}/{timeLimit}"
                            ) {
                                inclusive = true
                            }
                        }
                    },
                    onCreateNewQuiz = {
                        navController.navigate(
                            NavigationDestinations.CreateQuizFirstScreen.name
                        ) {
                            popUpTo(
                                route = "${NavigationDestinations.FinishedQuizScreen.name}/{score}/{totalScore}/{numCorrectAnswers}/{numQuestions}/{timeLimit}"
                            ) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
        composable(NavigationDestinations.QuizTemplates.name) {
            val quizTemplatesViewModel = hiltViewModel<QuizTemplatesViewModel>()
            val playQuizTemplateDialogViewModel = hiltViewModel<PlayQuizTemplateDialogViewModel>()
            val quizTemplateDialogViewModel = hiltViewModel<QuizTemplateDialogViewModel>()
            QuizTemplatesScreen(
                quizTemplatesViewModel = quizTemplatesViewModel,
                playQuizTemplateDialogViewModel = playQuizTemplateDialogViewModel,
                quizTemplateDialogViewModel = quizTemplateDialogViewModel,
                onNavigateToPlayQuiz = { timeLimit, categoryId, difficultyId, numOfQuestions ->
                    navController.navigate(
                        "${NavigationDestinations.PlayQuizScreen.name}/$timeLimit/true?categoryId=$categoryId&difficultyId=$difficultyId&numOfQuestions=$numOfQuestions"
                    )
                }
            )
        }
    }
}