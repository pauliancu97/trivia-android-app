package com.example.triviaapp.ui.utils

inline fun <reified T: Enum<T>> valueOrNullOf(name: String): T? =
    enumValues<T>().firstOrNull { it.name == name }