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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.triviaapp.ui.navigation.NavigationDestinations
import com.example.triviaapp.ui.screens.createquiz.CreateQuizFirstScreen
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
                }
            )
        }
        composable(NavigationDestinations.CreateQuizFirstScreen.name) {
            CreateQuizFirstScreen()
        }
    }
}