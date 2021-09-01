package com.example.triviaapp.ui.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.triviaapp.ui.models.Difficulty

@Entity(tableName = QuestionBooleanEntity.TABLE_NAME)
data class QuestionBooleanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = TEXT) val text: String,
    @ColumnInfo(name = CATEGORY_ID) val categoryId: Int,
    @ColumnInfo(name = DIFFICULTY) val difficulty: Difficulty,
    @ColumnInfo(name = CORRECT_ANSWER) val correctAnswer: Boolean
) {
    companion object {
        const val TABLE_NAME = "question_boolean_entity"
        const val TEXT = "text"
        const val CATEGORY_ID = "category_id"
        const val DIFFICULTY = "difficulty"
        const val CORRECT_ANSWER = "correct_answer"
    }
}
