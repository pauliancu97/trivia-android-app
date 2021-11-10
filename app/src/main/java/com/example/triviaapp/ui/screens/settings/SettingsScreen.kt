package com.example.triviaapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triviaapp.R
import com.example.triviaapp.ui.models.ThemeSetting
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun SettingsScreen(
    themeSettingsViewModel: ThemeSettingsScreenViewModel,
    questionNotificationSettingsViewModel: QuestionNotificationSettingsViewModel
) {
    val themeSettingState by themeSettingsViewModel.stateFlow.collectAsState(SettingsScreenState())
    Column {
        ThemeSettingsScreen(
            settingsScreenState = themeSettingState,
            onThemeSettingClicked = { themeSettingsViewModel.showThemeSettingDialog() },
            onDismissThemeSettingDialog = { themeSettingsViewModel.hideThemeSettingDialog() },
            onThemeSettingSelected = {
                themeSettingsViewModel.setThemeSetting(it)
                themeSettingsViewModel.hideThemeSettingDialog()
            }
        )
        QuestionNotificationSettingsScreen(questionNotificationSettingsViewModel)
    }
}

@Composable
fun ThemeSettingsScreen(
    settingsScreenState: SettingsScreenState,
    onThemeSettingClicked: () -> Unit,
    onDismissThemeSettingDialog: () -> Unit,
    onThemeSettingSelected: (ThemeSetting) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
    ) {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .padding(top = 5.dp)
                .clickable { onThemeSettingClicked() },
            elevation = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.theme),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = stringResource(settingsScreenState.themeSetting.textId),
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
    if (settingsScreenState.isThemeSettingDialogShown) {
        AlertDialog(
            onDismissRequest = { onDismissThemeSettingDialog() },
            title = {
                Text(
                    text = stringResource(R.string.selected_theme),
                    style = MaterialTheme.typography.h5
                )
            },
            buttons = {
                for (themeSetting in ThemeSetting.values()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .clickable { onThemeSettingSelected(themeSetting) }
                    ) {
                        RadioButton(
                            selected = themeSetting == settingsScreenState.themeSetting,
                            onClick = { onThemeSettingSelected(themeSetting) }
                        )
                        Text(
                            text = stringResource(themeSetting.textId)
                        )
                    }
                }
            },
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
        )
    }
}

@Composable
@Preview
fun PreviewSettingsScreenNoAlert() {
    ThemeSettingsScreen(
        settingsScreenState = SettingsScreenState(
            themeSetting = ThemeSetting.SystemDefault,
            isThemeSettingDialogShown = false
        ),
        onThemeSettingClicked = {},
        onDismissThemeSettingDialog = {},
        onThemeSettingSelected = {}
    )
}

@Composable
@Preview
fun PreviewSettingsScreenWithAlert() {
    ThemeSettingsScreen(
        settingsScreenState = SettingsScreenState(
            themeSetting = ThemeSetting.SystemDefault,
            isThemeSettingDialogShown = true
        ),
        onThemeSettingClicked = {},
        onDismissThemeSettingDialog = {},
        onThemeSettingSelected = {}
    )
}