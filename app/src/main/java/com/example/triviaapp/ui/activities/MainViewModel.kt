package com.example.triviaapp.ui.activities

import androidx.lifecycle.ViewModel
import com.example.triviaapp.ui.repositories.SettingsRepository
import com.example.triviaapp.ui.workers.QuestionNotificationWorkerRequester
import com.example.triviaapp.ui.workers.UpdateCategoriesWorkerRequester
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val updateCategoriesWorkerRequester: UpdateCategoriesWorkerRequester,
    private val questionNotificationWorkerRequester: QuestionNotificationWorkerRequester
): ViewModel() {

    fun themeSettingFlow() = settingsRepository.themeSettingFlow()

    fun getThemeSetting() = settingsRepository.themeSetting

    fun enqueueWorkers() {
        updateCategoriesWorkerRequester.enqueue()
        questionNotificationWorkerRequester.enqueue()
    }
}