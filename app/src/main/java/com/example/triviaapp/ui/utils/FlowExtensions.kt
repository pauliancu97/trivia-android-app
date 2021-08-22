package com.example.triviaapp.ui.utils

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.update(body: T.() -> T) {
    value = value.body()
}