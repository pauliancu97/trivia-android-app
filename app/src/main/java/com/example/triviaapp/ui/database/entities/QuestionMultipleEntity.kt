package com.example.triviaapp.ui.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.triviaapp.ui.models.Difficulty

@Entity(tableName = QuestionMultipleEntity.TABLE_NAME)
data class QuestionMultipleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = TEXT) val text: String,
    @ColumnInfo(name = CATEGORY_ID) val categoryId: Int,
    @ColumnInfo(name = DIFFICULTY) val difficulty: Difficulty
) {
    companion object {
        const val TABLE_NAME = "question_multiple_entity"
        const val TEXT = "text"
        const val CATEGORY_ID = "category_id"
        const val DIFFICULTY = "difficulty"
    }
}