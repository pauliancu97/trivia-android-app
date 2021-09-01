package com.example.triviaapp.ui.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.triviaapp.ui.database.daos.AnswerDao
import com.example.triviaapp.ui.database.daos.CategoryDao
import com.example.triviaapp.ui.database.daos.QuestionDao
import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.database.entities.QuestionAnswerEntity
import com.example.triviaapp.ui.database.entities.QuestionBooleanEntity
import com.example.triviaapp.ui.database.entities.QuestionMultipleEntity

@Database(
    entities = [
        CategoryEntity::class,
        QuestionMultipleEntity::class,
        QuestionAnswerEntity::class,
        QuestionBooleanEntity::class
    ],
    version = 4
)
@TypeConverters(Converters::class)
abstract class TriviaDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun questionDao(): QuestionDao

    abstract fun answerDao(): AnswerDao

    companion object {
        const val DATABASE_NAME = "trivia_database"
    }
}