package com.example.triviaapp.ui.activities

import androidx.lifecycle.ViewModel
import com.example.triviaapp.ui.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {

    fun themeSettingFlow() = settingsRepository.themeSettingFlow()

}