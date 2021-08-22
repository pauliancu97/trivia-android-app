package com.example.triviaapp.ui.repositories

import com.example.triviaapp.ui.database.daos.CategoryDao
import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.network.services.TriviaService
import com.example.triviaapp.ui.utils.toModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TriviaRepository @Inject constructor(
    private val triviaService: TriviaService,
    private val categoryDao: CategoryDao
){

    suspend fun fetchCategories() {
        val categoriesSummaries = triviaService.getCategoriesLookupResponse()
        val categoriesQuestionsCount = categoriesSummaries.categories
            .map { category ->
                triviaService.getCategoryQuestionCountResponse(category.id)
            }
        val categoriesIds = categoriesSummaries.categories.map { it.id }
        val categoriesNamesById = categoriesSummaries.categories
            .associateBy { it.id }
            .mapValues { (_, category) -> category.name }
        val categoriesQuestionsCountById = categoriesQuestionsCount
            .associateBy { it.categoryId }
        val categoriesEntities = categoriesIds
            .mapNotNull { id ->
                val categoryName = categoriesNamesById[id] ?: return@mapNotNull null
                val categoryQuestionCount = categoriesQuestionsCountById[id]?.questionsCount ?: return@mapNotNull null
                CategoryEntity(
                    id = id,
                    name = categoryName,
                    numOfQuestions = categoryQuestionCount.numOfQuestions,
                    numOfEasyQuestions = categoryQuestionCount.numOfEasyQuestions,
                    numOfMediumQuestions = categoryQuestionCount.numOfNormalQuestions,
                    numOfHardQuestions = categoryQuestionCount.numOfHardQuestions
                )
            }
        categoryDao.insertAll(categoriesEntities)
    }

    suspend fun getCategories() = categoryDao.getCategories()
        .map { it.toModel() }

    fun getCategoriesFlow() = categoryDao.getCategoriesFlow()
        .map { it.map { categoryEntity -> categoryEntity.toModel() } }
}