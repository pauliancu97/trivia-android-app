package com.example.triviaapp.ui.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.triviaapp.ui.models.ThemeSetting
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    companion object {
        private const val SETTINGS_PREFERENCE_KEY = "SETTINGS_PREFERENCE_KEY"
        private const val THEME_SETTING_KEY = "THEME_SETTING_KEY"
    }

    private val settingsSharedPreferences: SharedPreferences = context.getSharedPreferences(
        SETTINGS_PREFERENCE_KEY,
        Context.MODE_PRIVATE
    )

    @ExperimentalCoroutinesApi
    fun themeSettingFlow(): Flow<ThemeSetting> = callbackFlow {
        val callback = object : SharedPreferences.OnSharedPreferenceChangeListener {
            override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
                trySendBlocking(themeSetting)
            }
        }
        settingsSharedPreferences.registerOnSharedPreferenceChangeListener(callback)
        awaitClose { settingsSharedPreferences.unregisterOnSharedPreferenceChangeListener(callback) }
    }

    var themeSetting: ThemeSetting
        get() = settingsSharedPreferences.getString(THEME_SETTING_KEY, null)
            ?.let { ThemeSetting.values().firstOrNull { themeSetting -> themeSetting.name == it } }
            ?: ThemeSetting.SystemDefault
        set(value) {
            settingsSharedPreferences
                .edit()
                .putString(THEME_SETTING_KEY, value.name)
                .apply()
        }
}