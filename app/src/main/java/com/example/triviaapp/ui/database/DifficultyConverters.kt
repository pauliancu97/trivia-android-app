package com.example.triviaapp.ui.database

import androidx.room.TypeConverter
import com.example.triviaapp.ui.database.entities.DifficultyOptionEntity
import com.example.triviaapp.ui.models.Difficulty

class DifficultyConverters {
    @TypeConverter
    fun toDifficulty(ordinal: Int?): Difficulty? =
        ordinal?.let { Difficulty.values().getOrNull(it) }

    @TypeConverter
    fun fromDifficulty(difficulty: Difficulty?): Int? = difficulty?.ordinal

    @TypeConverter
    fun toDifficultyOption(ordinal: Int?): DifficultyOptionEntity? =
        ordinal?.let { DifficultyOptionEntity.values().getOrNull(it) }

    @TypeConverter
    fun fromDifficultyOption(difficultyOption: DifficultyOptionEntity?) =
        difficultyOption?.ordinal
}