package com.example.triviaapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.ui.screens.start.StartScreen
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
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun TriviaApp() {
    TriviaAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            StartScreen(onPlayClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TriviaApp()
}