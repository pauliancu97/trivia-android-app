package com.example.triviaapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.ThemeSetting
import com.example.triviaapp.ui.repositories.SettingsRepository
import com.example.triviaapp.ui.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsScreenState(
    val themeSetting: ThemeSetting = ThemeSetting.SystemDefault,
    val isThemeSettingDialogShown: Boolean = false
)

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {

    private val mutableStateFlow: MutableStateFlow<SettingsScreenState> =
        MutableStateFlow(SettingsScreenState())

    val stateFlow: Flow<SettingsScreenState> = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.themeSettingFlow()
                .collect {
                    mutableStateFlow.update { copy(themeSetting = it) }
                }
        }
    }

    fun setThemeSetting(themeSetting: ThemeSetting) {
        settingsRepository.themeSetting = themeSetting
    }

    fun showThemeSettingDialog() {
        mutableStateFlow.update { copy(isThemeSettingDialogShown = true) }
    }

    fun hideThemeSettingDialog() {
        mutableStateFlow.update { copy(isThemeSettingDialogShown = false) }
    }
}