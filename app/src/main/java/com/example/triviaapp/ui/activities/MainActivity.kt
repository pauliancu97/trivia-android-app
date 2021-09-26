package com.example.triviaapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.triviaapp.R
import com.example.triviaapp.ui.animations.EnterFromRightAnimation
import com.example.triviaapp.ui.dialogs.deletequiztemplate.DeleteQuizTemplateViewModel
import com.example.triviaapp.ui.dialogs.playquiztemplate.PlayQuizTemplateDialogViewModel
import com.example.triviaapp.ui.dialogs.savequiztemplate.QuizTemplateDialogViewModel
import com.example.triviaapp.ui.drawer.Drawer
import com.example.triviaapp.ui.drawer.DrawerDestination
import com.example.triviaapp.ui.drawer.TopMenuAppBar
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
import kotlinx.coroutines.launch

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
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val navController = rememberNavController()
        val navState = navController.currentBackStackEntryAsState()
        val selectedDrawerDestination = when (navState.value?.destination?.route) {
            NavigationDestinations.StartScreen.name -> DrawerDestination.Home
            NavigationDestinations.Settings.name -> DrawerDestination.Settings
            else -> null
        }
        val openDrawer = {
            scope.launch { drawerState.open() }
            Unit
        }
        val closeDrawer = {
            scope.launch { drawerState.close() }
            Unit
        }
        TriviaAppNavHost(
            selectedDrawerDestination = selectedDrawerDestination,
            drawerState = drawerState,
            openDrawer = openDrawer,
            closeDrawer = closeDrawer,
            navController = navController
        )
    }
}

@Composable
fun TriviaAppNavHost(
    selectedDrawerDestination: DrawerDestination? = null,
    drawerState: DrawerState,
    openDrawer: () -> Unit,
    closeDrawer: () -> Unit,
    navController: NavHostController
) {
    Surface(color = MaterialTheme.colors.background) {
        ModalDrawer(
            drawerContent = {
                Drawer(
                    selectedDrawerDestination = selectedDrawerDestination,
                    onDrawerItemClicked = {
                        navController.navigate(it.navigationDestination.name) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        closeDrawer()
                    }
                )
            },
            gesturesEnabled = drawerState.isOpen,
            drawerState = drawerState
        ) {
            NavHost(
                navController = navController,
                startDestination = NavigationDestinations.StartScreen.name
            ) {
                composable(NavigationDestinations.StartScreen.name) {
                    val viewModel = hiltViewModel<StartScreenViewModel>()
                    Column {
                        TopMenuAppBar(openDrawer)
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
                    val isFromQuizTemplatesScreen = navController.previousBackStackEntry
                        ?.destination?.route == NavigationDestinations.QuizTemplates.name
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
                                    if (isFromQuizTemplatesScreen) {
                                        popUpTo(
                                            route = NavigationDestinations.QuizTemplates.name
                                        )
                                    } else {
                                        popUpTo(
                                            route = NavigationDestinations.CreateQuizFirstScreen.name
                                        ) {
                                            inclusive = true
                                        }
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
                    val deleteQuizTemplateViewModel = hiltViewModel<DeleteQuizTemplateViewModel>()
                    QuizTemplatesScreen(
                        quizTemplatesViewModel = quizTemplatesViewModel,
                        playQuizTemplateDialogViewModel = playQuizTemplateDialogViewModel,
                        quizTemplateDialogViewModel = quizTemplateDialogViewModel,
                        deleteQuizTemplateViewModel = deleteQuizTemplateViewModel,
                        onNavigateToPlayQuiz = { timeLimit, categoryId, difficultyId, numOfQuestions ->
                            navController.navigate(
                                "${NavigationDestinations.PlayQuizScreen.name}/$timeLimit/true?categoryId=$categoryId&difficultyId=$difficultyId&numOfQuestions=$numOfQuestions"
                            )
                        }
                    )
                }
                composable(NavigationDestinations.Settings.name) {
                    Column {
                        TopMenuAppBar(openDrawer)
                        Text(text = stringResource(R.string.Settings))
                    }
                }
            }
        }
    }
}