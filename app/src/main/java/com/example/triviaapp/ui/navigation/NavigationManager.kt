package com.example.triviaapp.ui.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationManager {

    private val destinationMutableLiveData: MutableLiveData<String> = MutableLiveData(NavigationDestinations.StartScreen.name)

    private fun navigate(destination: String) {
        destinationMutableLiveData.value = destination
    }

    fun navigateToCreateQuizFirstScreen() {
        navigate(NavigationDestinations.CreateQuizFirstScreen.name)
    }

    fun destinationLiveData(): LiveData<String> = destinationMutableLiveData
}