package com.example.triviaapp.ui.utils

import android.content.SharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun SharedPreferences.getBooleanFlow(preferenceKey: String): Flow<Boolean> = callbackFlow {
    val callback = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (preferenceKey == key) {
                val value = getBoolean(preferenceKey, false)
                trySend(value)
            }
        }
    }
    registerOnSharedPreferenceChangeListener(callback)
    awaitClose { unregisterOnSharedPreferenceChangeListener(callback) }
}

@ExperimentalCoroutinesApi
fun SharedPreferences.getStringFlow(preferenceKey: String): Flow<String> = callbackFlow {
    val callback = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (preferenceKey == key) {
                val value = getString(preferenceKey, null)
                value?.let { trySend(it) }
            }
        }
    }
    registerOnSharedPreferenceChangeListener(callback)
    awaitClose { unregisterOnSharedPreferenceChangeListener(callback) }
}

@ExperimentalCoroutinesApi
fun SharedPreferences.getLongFlow(preferenceKey: String): Flow<Long> = callbackFlow {
    val callback = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (preferenceKey == key) {
                val value = getLong(preferenceKey, 0L)
                trySend(value)
            }
        }
    }
    registerOnSharedPreferenceChangeListener(callback)
    awaitClose { unregisterOnSharedPreferenceChangeListener(callback) }
}

@ExperimentalCoroutinesApi
fun SharedPreferences.getIntFlow(preferenceKey: String): Flow<Int> = callbackFlow {
    val callback = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (preferenceKey == key) {
                val value = getInt(preferenceKey, 0)
                trySend(value)
            }
        }
    }
    registerOnSharedPreferenceChangeListener(callback)
    awaitClose { unregisterOnSharedPreferenceChangeListener(callback) }
}