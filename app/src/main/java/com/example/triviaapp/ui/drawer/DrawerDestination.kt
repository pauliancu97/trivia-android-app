package com.example.triviaapp.ui.drawer

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.triviaapp.R
import com.example.triviaapp.ui.navigation.NavigationDestinations

enum class DrawerDestination(
    val icon: ImageVector,
    @StringRes val descriptionId: Int,
    val navigationDestination: NavigationDestinations
) {
    Home(Icons.Default.Home, R.string.home, NavigationDestinations.StartScreen),
    Settings(Icons.Default.Settings, R.string.Settings, NavigationDestinations.Settings)
}