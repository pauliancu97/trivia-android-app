package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.database.entities.QuizTemplateEntity
import com.example.triviaapp.ui.database.entities.QuizTemplateResult

@Dao
interface QuizTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quizTemplateEntity: QuizTemplateEntity)

    @Query("SELECT template.name AS name, category.name AS category_name, template.category_id AS category_id, template.difficulty_option AS difficulty, template.num_of_questions AS num_questions, template.time_limit AS time_limit  FROM ${QuizTemplateEntity.TABLE_NAME} template LEFT JOIN ${CategoryEntity.TABLE_NAME} category ON template.category_id = category.id")
    suspend fun getQuizTemplates(): List<QuizTemplateResult>

    @Query("SELECT COUNT(*) FROM ${QuizTemplateEntity.TABLE_NAME} WHERE name = :name")
    suspend fun getNumOfQuizTemplatesWithName(name: String): Int
}