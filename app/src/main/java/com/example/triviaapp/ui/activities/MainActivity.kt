package com.example.triviaapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.triviaapp.ui.models.DifficultyOption
import com.example.triviaapp.ui.navigation.NavigationDestinations
import com.example.triviaapp.ui.screens.createquiz.CreateQuizFirstScreen
import com.example.triviaapp.ui.screens.createquiz.CreateQuizFirstScreenViewModel
import com.example.triviaapp.ui.screens.createquiz.CreateQuizSecondScreen
import com.example.triviaapp.ui.screens.createquiz.CreateQuizSecondScreenViewModel
import com.example.triviaapp.ui.screens.playquiz.PlayQuizScreen
import com.example.triviaapp.ui.screens.playquiz.PlayQuizViewModel
import com.example.triviaapp.ui.screens.playquiz.PlayQuizViewModelFactory
import com.example.triviaapp.ui.screens.start.StartScreen
import com.example.triviaapp.ui.screens.start.StartScreenViewModel
import com.example.triviaapp.ui.theme.TriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var playQuizViewModelFactory: PlayQuizViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaApp(playQuizViewModelFactory)
        }
    }
}

@Composable
fun TriviaApp(
    playQuizViewModelFactory: PlayQuizViewModelFactory
) {
    TriviaAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            TriviaAppNavHost(playQuizViewModelFactory)
        }
    }
}

@Composable
fun TriviaAppNavHost(
    playQuizViewModelFactory: PlayQuizViewModelFactory
) {
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
                }
            )
        }
        composable(NavigationDestinations.CreateQuizFirstScreen.name) {
            val viewModel = hiltViewModel<CreateQuizFirstScreenViewModel>()
            CreateQuizFirstScreen(
                viewModel,
                onNavigateToCreateQuizSecondScreen = { categoryId, difficulty, numQuestions ->
                    navController.navigate(
                        "${NavigationDestinations.CreateQuizSecondScreen}?categoryId=$categoryId&difficulty=$difficulty&numQuestions=$numQuestions"
                    )
                }
            )
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
            viewModel.initialize(categoryId, difficulty, maxNumOfQuestions)
            CreateQuizSecondScreen(
                viewModel = viewModel,
                onNavigateToPlayQuiz = { timeLimit, categoryId, difficultyId, numOfQuestions ->
                    navController.navigate(
                        "${NavigationDestinations.PlayQuizScreen.name}/$timeLimit/$categoryId/$difficultyId/$numOfQuestions/true"
                    )
                }
            )
        }
        composable(
            route = "${NavigationDestinations.PlayQuizScreen.name}/{timeLimit}/{categoryId}/{difficultyId}/{numOfQuestions}/{shouldFetchQuestions}",
            arguments = listOf(
                navArgument(
                    name = "timeLimit",
                ) {
                    type = NavType.IntType
                },
                navArgument(
                    name = "categoryId",
                ) {
                    type = NavType.IntType
                },
                navArgument(
                    name = "difficultyId",
                ) {
                    type = NavType.IntType
                },
                navArgument(
                    name = "numOfQuestions",
                ) {
                    type = NavType.IntType
                },
                navArgument(name = "shouldFetchQuestions") {
                    type = NavType.BoolType
                }
            )
        ) {
            val timeLimit = it.arguments?.getInt("timeLimit") ?: 0
            val categoryId = it.arguments?.getInt("categoryId") ?: 0
            val difficulty = it.arguments?.getInt("difficultyId")?.let { id -> DifficultyOption.values().getOrNull(id) }
            val numOfQuestions = it.arguments?.getInt("numOfQuestions") ?: 0
            val shouldFetchQuestions = it.arguments?.getBoolean("shouldFetchQuestions", true) ?: true
            val viewModel = viewModel<PlayQuizViewModel>(
                factory = playQuizViewModelFactory.create(
                    timeLimit,
                    categoryId,
                    difficulty?.toDifficulty(),
                    numOfQuestions,
                    shouldFetchQuestions
                )
            )
            PlayQuizScreen(viewModel)
        }
    }
}