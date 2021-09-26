package com.example.triviaapp.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.R
import com.example.triviaapp.ui.models.ThemeSetting

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel
) {
    val state by viewModel.stateFlow.collectAsState(SettingsScreenState())
    SettingsScreen(
        settingsScreenState = state,
        onThemeSettingClicked = {  viewModel.showThemeSettingDialog() },
        onDismissThemeSettingDialog = { viewModel.hideThemeSettingDialog() },
        onThemeSettingSelected = {
            viewModel.setThemeSetting(it)
            viewModel.hideThemeSettingDialog()
        }
    )
}

@Composable
fun SettingsScreen(
    settingsScreenState: SettingsScreenState,
    onThemeSettingClicked: () -> Unit,
    onDismissThemeSettingDialog: () -> Unit,
    onThemeSettingSelected: (ThemeSetting) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onThemeSettingClicked() }
    ) {
        Text(
            text = stringResource(R.string.Settings),
            style = MaterialTheme.typography.h5
        )
        Text(
            text = stringResource(settingsScreenState.themeSetting.textId)
        )
    }
    if (settingsScreenState.isThemeSettingDialogShown) {
        AlertDialog(
            onDismissRequest = { onDismissThemeSettingDialog() },
            title = {
                Text(
                    text = stringResource(R.string.selected_theme)
                )
            },
            buttons = {
                for (themeSetting in ThemeSetting.values()) {
                    Row(
                        modifier = Modifier.clickable {
                            onThemeSettingSelected(themeSetting)
                        }
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
            }
        )
    }
}

@Composable
@Preview
fun PreviewSettingsScreenNoAlert() {
    SettingsScreen(
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
    SettingsScreen(
        settingsScreenState = SettingsScreenState(
            themeSetting = ThemeSetting.SystemDefault,
            isThemeSettingDialogShown = true
        ),
        onThemeSettingClicked = {},
        onDismissThemeSettingDialog = {},
        onThemeSettingSelected = {}
    )
}