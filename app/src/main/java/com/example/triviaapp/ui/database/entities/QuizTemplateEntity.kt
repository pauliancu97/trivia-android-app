package com.example.triviaapp.ui.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = QuizTemplateEntity.TABLE_NAME
)
data class QuizTemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = CATEGORY_ID) val categoryId: Int?,
    @ColumnInfo(name = DIFFICULTY_OPTION) val difficultyOption: DifficultyOptionEntity,
    @ColumnInfo(name = NUM_OF_QUESTIONS) val numOfQuestions: Int,
    @ColumnInfo(name = TIME_LIMIT) val timeLimit: Int,
    @ColumnInfo(name = NAME) val name: String
) {
    companion object {
        const val TABLE_NAME = "quiz_template"
        const val ID = "id"
        const val CATEGORY_ID = "category_id"
        const val DIFFICULTY_OPTION = "difficulty_option"
        const val NUM_OF_QUESTIONS = "num_of_questions"
        const val TIME_LIMIT = "time_limit"
        const val NAME = "name"
    }
}