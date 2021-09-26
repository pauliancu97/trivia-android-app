package com.example.triviaapp.ui.models

import androidx.annotation.StringRes
import com.example.triviaapp.R

enum class ThemeSetting(
    @StringRes val textId: Int
) {
    SystemDefault(R.string.system_default),
    Light(R.string.light),
    Dark(R.string.dark)
}