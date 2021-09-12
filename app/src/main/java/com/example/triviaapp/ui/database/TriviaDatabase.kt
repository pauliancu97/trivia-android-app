package com.example.triviaapp.ui.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.triviaapp.ui.database.daos.AnswerDao
import com.example.triviaapp.ui.database.daos.CategoryDao
import com.example.triviaapp.ui.database.daos.QuestionDao
import com.example.triviaapp.ui.database.daos.QuizTemplateDao
import com.example.triviaapp.ui.database.entities.*

@Database(
    entities = [
        CategoryEntity::class,
        QuestionMultipleEntity::class,
        QuestionAnswerEntity::class,
        QuestionBooleanEntity::class,
        QuizTemplateEntity::class
    ],
    version = 5
)
@TypeConverters(DifficultyConverters::class)
abstract class TriviaDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun questionDao(): QuestionDao

    abstract fun answerDao(): AnswerDao

    abstract fun quizTemplateDao(): QuizTemplateDao

    companion object {
        const val DATABASE_NAME = "trivia_database"
    }
}