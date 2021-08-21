package com.example.triviaapp.ui.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CategoryEntity.TABLE_NAME)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = ID_COLUMN) val id: Int,
    @ColumnInfo(name = NAME_COLUMN) val name: String,
    @ColumnInfo(name = NUM_OF_QUESTIONS_COLUMN) val numOfQuestions: Int,
    @ColumnInfo(name = NUM_OF_EASY_QUESTIONS_COLUMN) val numOfEasyQuestions: Int,
    @ColumnInfo(name = NUM_OF_MEDIUM_QUESTIONS_COLUMN) val numOfMediumQuestions: Int,
    @ColumnInfo(name = NUM_OF_HARD_QUESTIONS_COLUMN) val numOfHardQuestions: Int
) {
    companion object {
        const val TABLE_NAME = "category"
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "name"
        const val NUM_OF_QUESTIONS_COLUMN = "num_questions"
        const val NUM_OF_EASY_QUESTIONS_COLUMN = "num_easy_questions"
        const val NUM_OF_MEDIUM_QUESTIONS_COLUMN = "num_medium_questions"
        const val NUM_OF_HARD_QUESTIONS_COLUMN = "num_hard_questions"
    }
}