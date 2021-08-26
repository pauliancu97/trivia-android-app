package com.example.triviaapp.ui.repositories

import com.example.triviaapp.ui.database.daos.AnswerDao
import com.example.triviaapp.ui.database.daos.CategoryDao
import com.example.triviaapp.ui.database.daos.QuestionDao
import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.database.entities.QuestionAnswerEntity
import com.example.triviaapp.ui.database.entities.QuestionBooleanEntity
import com.example.triviaapp.ui.database.entities.QuestionMultipleEntity
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.Question
import com.example.triviaapp.ui.models.QuestionType
import com.example.triviaapp.ui.network.models.QuestionResponse
import com.example.triviaapp.ui.network.services.TriviaService
import com.example.triviaapp.ui.utils.toModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TriviaRepository @Inject constructor(
    private val triviaService: TriviaService,
    private val categoryDao: CategoryDao,
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao
){

    companion object {
        private const val NUM_MAX_QUESTIONS_PER_CALL = 50
    }

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

    suspend fun getCategoryById(id: Int) =
        categoryDao.getCategoryById(id)?.toModel()

    fun getCategoriesFlow() = categoryDao.getCategoriesFlow()
        .map { it.map { categoryEntity -> categoryEntity.toModel() } }

    suspend fun fetchQuestions(category: Category?, difficulty: Difficulty?, numOfQuestion: Int) {
        val token = triviaService.getTokenResponse().token
        val questionsResponses = mutableListOf<QuestionResponse>()
        val numLimitApiCalls = numOfQuestion / NUM_MAX_QUESTIONS_PER_CALL
        for (index in 1..numLimitApiCalls) {
            val questionLookupResponse = triviaService.getQuestionsLookupResponse(
                difficulty = difficulty?.apiString(),
                category = category?.name,
                numOfQuestions = NUM_MAX_QUESTIONS_PER_CALL,
                token = token
            )
            questionsResponses.addAll(questionLookupResponse.results)
        }
        val restNumOfQuestions = numOfQuestion % NUM_MAX_QUESTIONS_PER_CALL
        if (restNumOfQuestions != 0) {
            val questionLookupResponse = triviaService.getQuestionsLookupResponse(
                difficulty = difficulty?.apiString(),
                category = category?.name,
                numOfQuestions = restNumOfQuestions,
                token = token
            )
            questionsResponses.addAll(questionLookupResponse.results)
        }
        val questionsModels = questionsResponses.mapNotNull { it.toQuestion() }
        questionsModels.forEach { insertQuestionInDatabase(it) }
    }

    private suspend fun QuestionResponse.toQuestion(): Question? {
        val difficulty = Difficulty.fromString(this.difficulty) ?: return null
        val category = categoryDao.getCategoryByName(this.category)?.toModel() ?: return null
        val questionType = QuestionType.fromString(this.type) ?: return null
        return when (questionType) {
            QuestionType.Multiple -> Question.QuestionMultiple(
                text = question,
                category = category,
                difficulty = difficulty,
                answers = incorrectAnswers + correctAnswer,
                correctAnswer = correctAnswer
            )
            QuestionType.Boolean -> {
                val incorrectAnswer = incorrectAnswers.firstOrNull()?.toBoolean()
                val correctAnswer = correctAnswer.toBoolean()
                if (incorrectAnswer != null && correctAnswer != null) {
                    Question.QuestionBoolean(
                        text = question,
                        category = category,
                        difficulty = difficulty,
                        correctAnswer = correctAnswer
                    )
                } else {
                    null
                }
            }
        }
    }

    private suspend fun insertQuestionInDatabase(question: Question) {
        when (question) {
            is Question.QuestionMultiple -> {
                val questionEntity = QuestionMultipleEntity(
                    text = question.text,
                    categoryId = question.category.id,
                    difficulty = question.difficulty
                )
                val questionId = questionDao.insert(questionEntity).toInt()
                val answersEntity = question.answers.map { answer ->
                    val isCorrect = answer == question.correctAnswer
                    QuestionAnswerEntity(
                        text = answer,
                        questionId = questionId,
                        isCorrect = isCorrect
                    )
                }
                answersEntity.forEach {
                    answerDao.insert(it)
                }
            }
            is Question.QuestionBoolean -> {
                val questionEntity = QuestionBooleanEntity(
                    text = question.text,
                    categoryId = question.category.id,
                    difficulty = question.difficulty,
                    correctAnswer = question.correctAnswer
                )
                questionDao.insert(questionEntity)
            }
        }
    }

    private fun String.toBoolean() = when (this) {
        "True" -> true
        "False" -> false
        else -> null
    }
}