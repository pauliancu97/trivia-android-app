package com.example.triviaapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.example.triviaapp.ui.models.ThemeSetting

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TriviaAppTheme(themeSetting: ThemeSetting, content: @Composable() () -> Unit) {
    val colors = when (themeSetting) {
        ThemeSetting.SystemDefault -> {
            if (isSystemInDarkTheme()) {
                DarkColorPalette
            } else {
                LightColorPalette
            }
        }
        ThemeSetting.Light -> LightColorPalette
        ThemeSetting.Dark -> DarkColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}