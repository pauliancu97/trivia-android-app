package com.example.triviaapp.ui.repositories

import androidx.room.withTransaction
import com.example.triviaapp.ui.database.TriviaDatabase
import com.example.triviaapp.ui.database.daos.AnswerDao
import com.example.triviaapp.ui.database.daos.CategoryDao
import com.example.triviaapp.ui.database.daos.QuestionDao
import com.example.triviaapp.ui.database.daos.QuizTemplateDao
import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.database.entities.QuestionAnswerEntity
import com.example.triviaapp.ui.database.entities.QuestionBooleanEntity
import com.example.triviaapp.ui.database.entities.QuestionMultipleEntity
import com.example.triviaapp.ui.models.*
import com.example.triviaapp.ui.network.models.QuestionResponse
import com.example.triviaapp.ui.network.services.TriviaService
import com.example.triviaapp.ui.utils.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.URLDecoder
import javax.inject.Inject

class TriviaRepository @Inject constructor(
    private val triviaService: TriviaService,
    private val categoryDao: CategoryDao,
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao,
    private val quizTemplateDao: QuizTemplateDao,
    private val triviaDatabase: TriviaDatabase
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

    suspend fun fetchQuestions(category: Category?, difficulty: Difficulty?, numOfQuestions: Int): List<Question> {
        val token = triviaService.getTokenResponse().token
        val questionsResponses = mutableListOf<QuestionResponse>()
        val numLimitApiCalls = numOfQuestions / NUM_MAX_QUESTIONS_PER_CALL
        for (index in 1..numLimitApiCalls) {
            val questionLookupResponse = triviaService.getQuestionsLookupResponse(
                difficulty = difficulty?.apiString(),
                category = category?.id,
                numOfQuestions = NUM_MAX_QUESTIONS_PER_CALL,
                token = token
            )
            questionsResponses.addAll(questionLookupResponse.results)
        }
        val restNumOfQuestions = numOfQuestions % NUM_MAX_QUESTIONS_PER_CALL
        if (restNumOfQuestions != 0) {
            val questionLookupResponse = triviaService.getQuestionsLookupResponse(
                difficulty = difficulty?.apiString(),
                category = category?.id,
                numOfQuestions = restNumOfQuestions,
                token = token
            )
            questionsResponses.addAll(questionLookupResponse.results)
        }
        val questionsModels = questionsResponses.mapNotNull { it.toQuestion() }
        questionsModels.forEach { insertQuestionInDatabase(it) }
        return questionsModels
    }

    private fun String.decode(): String =
        try {
            URLDecoder.decode(this, "UTF-8")
        } catch (t: Throwable) {
            this
        }

    private suspend fun QuestionResponse.toQuestion(): Question? {
        val difficulty = Difficulty.fromString(this.difficulty) ?: return null
        val category = categoryDao.getCategoryByName(this.category.decode())?.toModel() ?: return null
        val questionType = QuestionType.fromString(this.type) ?: return null
        return when (questionType) {
            QuestionType.Multiple -> Question.QuestionMultiple(
                text = question.decode(),
                category = category,
                difficulty = difficulty,
                answers = (incorrectAnswers + correctAnswer).map { it.decode() },
                correctAnswer = correctAnswer.decode()
            )
            QuestionType.Boolean -> {
                val incorrectAnswer = incorrectAnswers.firstOrNull()?.toBoolean()
                val correctAnswer = correctAnswer.toBoolean()
                if (incorrectAnswer != null && correctAnswer != null) {
                    Question.QuestionBoolean(
                        text = question.decode(),
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

    suspend fun getQuestions(): List<Question> {
        val questionsBoolean = questionDao.getAllQuestionBoolean()
            .map { it.toModel() }
        val questionsMultiple = questionDao.getAllQuestionsMultiple()
            .map { it.toModel() }
        return questionsBoolean + questionsMultiple
    }

    suspend fun deleteAllQuestions() {
        triviaDatabase.withTransaction {
            questionDao.deleteAllQuestions()
            answerDao.deleteAll()
        }
    }

    suspend fun isQuizTemplateWithName(name: String): Boolean =
        quizTemplateDao.getNumOfQuizTemplatesWithName(name) != 0

    suspend fun saveQuizTemplate(quizTemplate: QuizTemplate) {
        quizTemplateDao.insert(quizTemplate.toEntity())
    }

    private suspend fun QuestionBooleanEntity.toModel() =
        Question.QuestionBoolean(
            text = this.text,
            category = categoryDao.getCategoryById(this.categoryId)?.toModel()!!,
            difficulty = this.difficulty,
            correctAnswer = this.correctAnswer
        )

    private suspend fun QuestionMultipleEntity.toModel(): Question.QuestionMultiple {
        val questionAnswersEntities = answerDao.getAnswersForQuestionWithId(this.id)
        return Question.QuestionMultiple(
            text = this.text,
            category = categoryDao.getCategoryById(this.categoryId)?.toModel()!!,
            difficulty = this.difficulty,
            answers = questionAnswersEntities.map { it.text },
            correctAnswer = questionAnswersEntities.first { it.isCorrect }.text
        )
    }

    private fun String.toBoolean() = when (this) {
        "True" -> true
        "False" -> false
        else -> null
    }
}