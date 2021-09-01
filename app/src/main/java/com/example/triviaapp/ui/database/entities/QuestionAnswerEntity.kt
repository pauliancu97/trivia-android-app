package com.example.triviaapp.ui.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = QuestionAnswerEntity.TABLE_NAME)
data class QuestionAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = TEXT) val text: String,
    @ColumnInfo(name=  QUESTION_ID) val questionId: Int,
    @ColumnInfo(name = IS_CORRECT) val isCorrect: Boolean
) {
    companion object {
        const val TABLE_NAME = "question_answer"
        const val TEXT = "text"
        const val QUESTION_ID = "question_id"
        const val IS_CORRECT = "IS_CORRECT"
    }
}