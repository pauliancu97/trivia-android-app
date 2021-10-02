package com.example.triviaapp.ui.activities

import androidx.lifecycle.ViewModel
import com.example.triviaapp.ui.repositories.SettingsRepository
import com.example.triviaapp.ui.workers.UpdateCategoriesWorkerRequester
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val updateCategoriesWorkerRequester: UpdateCategoriesWorkerRequester
): ViewModel() {

    fun themeSettingFlow() = settingsRepository.themeSettingFlow()

    fun getThemeSetting() = settingsRepository.themeSetting

    fun enqueueUpdateCategoriesWorker() {
        updateCategoriesWorkerRequester.enqueue()
    }
}