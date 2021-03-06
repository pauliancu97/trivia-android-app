package com.example.triviaapp.ui.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.database.entities.QuizTemplateEntity
import com.example.triviaapp.ui.database.entities.QuizTemplateResult
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizTemplateDao {
    @Insert
    suspend fun insert(quizTemplateEntity: QuizTemplateEntity)

    @Query("SELECT template.name AS name, category.name AS category_name, template.category_id AS category_id, template.difficulty_option AS difficulty, template.num_of_questions AS num_questions, template.time_limit AS time_limit  FROM ${QuizTemplateEntity.TABLE_NAME} template LEFT JOIN ${CategoryEntity.TABLE_NAME} category ON template.category_id = category.id")
    suspend fun getQuizTemplates(): List<QuizTemplateResult>

    @Query("SELECT template.name AS name, category.name AS category_name, template.category_id AS category_id, template.difficulty_option AS difficulty, template.num_of_questions AS num_questions, template.time_limit AS time_limit  FROM ${QuizTemplateEntity.TABLE_NAME} template LEFT JOIN ${CategoryEntity.TABLE_NAME} category ON template.category_id = category.id")
    fun getQuizTemplatesFlow(): Flow<List<QuizTemplateResult>>

    @Query("SELECT COUNT(*) FROM ${QuizTemplateEntity.TABLE_NAME} WHERE name = :name")
    suspend fun getNumOfQuizTemplatesWithName(name: String): Int

    @Query("SELECT COUNT(*) FROM ${QuizTemplateEntity.TABLE_NAME}")
    suspend fun getNumOfQuizTemplates(): Int

    @Query("UPDATE ${QuizTemplateEntity.TABLE_NAME} SET ${QuizTemplateEntity.NAME} = :name WHERE ${QuizTemplateEntity.ID} = :id")
    suspend fun updateQuizTemplateName(id: Int, name: String)

    @Query("SELECT ${QuizTemplateEntity.ID} FROM ${QuizTemplateEntity.TABLE_NAME} WHERE ${QuizTemplateEntity.NAME} = :name")
    suspend fun getQuizTemplatesIdsWithName(name: String): List<Int>

    @Query("SELECT template.name AS name, category.name AS category_name, template.category_id AS category_id, template.difficulty_option AS difficulty, template.num_of_questions AS num_questions, template.time_limit AS time_limit  FROM ${QuizTemplateEntity.TABLE_NAME} template LEFT JOIN ${CategoryEntity.TABLE_NAME} category ON template.category_id = category.id WHERE template.name = :name LIMIT 1")
    suspend fun getQuizTemplateWithName(name: String): QuizTemplateResult?

    @Query("SELECT ${QuizTemplateEntity.ID} FROM ${QuizTemplateEntity.TABLE_NAME} WHERE ${QuizTemplateEntity.NAME} = :name LIMIT 1")
    suspend fun getQuizTemplateIdWithName(name: String): Int?

    @Query("DELETE FROM ${QuizTemplateEntity.TABLE_NAME} WHERE ${QuizTemplateEntity.ID} = :id")
    suspend fun deleteQuizTemplate(id: Int)
}