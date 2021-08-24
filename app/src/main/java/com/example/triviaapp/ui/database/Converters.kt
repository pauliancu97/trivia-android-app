package com.example.triviaapp.ui.database

import androidx.room.TypeConverter
import com.example.triviaapp.ui.models.Difficulty

class Converters {
    @TypeConverter
    fun toDifficulty(ordinal: Int?): Difficulty? =
        ordinal?.let { Difficulty.values().getOrNull(it) }

    @TypeConverter
    fun fromDifficulty(difficulty: Difficulty?): Int? = difficulty?.ordinal
}