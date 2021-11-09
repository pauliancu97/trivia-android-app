package com.example.triviaapp.ui.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.triviaapp.BuildConfig
import com.example.triviaapp.ui.notifications.NotificationsHelper
import com.example.triviaapp.ui.repositories.QuestionNotificationRepository
import com.example.triviaapp.ui.repositories.TriviaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val QUESTION_NOTIFICATION_WORKER_NAME = "question_notification_worker"
private const val KEY_IS_DEBUG = "KEY_IS_DEBUG"

@HiltWorker
class QuestionNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val triviaRepository: TriviaRepository,
    private val questionNotificationRepository: QuestionNotificationRepository,
    private val questionNotificationWorkerRequester: QuestionNotificationWorkerRequester,
    private val notificationsHelper: NotificationsHelper
): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result =
        try {
            val question = triviaRepository.getSingleQuestionFromService(
                questionNotificationRepository.getCategory(),
                questionNotificationRepository.difficulty,
                questionNotificationRepository.questionType
            )
            if (question != null) {
                Timber.d("Question Notification $question")
                notificationsHelper.showQuestionNotification(question)
                val isDebug = inputData.getBoolean(KEY_IS_DEBUG, false)
                if (isDebug) {
                    delay(
                        TimeUnit.MILLISECONDS.convert(
                            questionNotificationRepository.period.periodValue,
                            questionNotificationRepository.period.periodTimeUnit
                        )
                    )
                    questionNotificationWorkerRequester.enqueue()
                }
                Result.success()
            } else {
                Result.failure()
            }
        } catch (t: Throwable) {
            Result.failure()
        }
}

class QuestionNotificationWorkerRequester @Inject constructor(
    @ApplicationContext private val context: Context,
    private val questionNotificationRepository: QuestionNotificationRepository
) {
    fun enqueue() {
        if (BuildConfig.DEBUG) {
            val workRequest = OneTimeWorkRequestBuilder<QuestionNotificationWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setInputData(
                    workDataOf(KEY_IS_DEBUG to true)
                )
                .setInitialDelay(
                    questionNotificationRepository.period.periodValue,
                    questionNotificationRepository.period.periodTimeUnit
                )
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    QUESTION_NOTIFICATION_WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    workRequest
                )
        } else {
            val workRequest = PeriodicWorkRequestBuilder<QuestionNotificationWorker>(
                questionNotificationRepository.period.periodValue,
                questionNotificationRepository.period.periodTimeUnit
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setInputData(
                    workDataOf(KEY_IS_DEBUG to false)
                )
                .setInitialDelay(
                    questionNotificationRepository.period.periodValue,
                    questionNotificationRepository.period.periodTimeUnit
                )
                .build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    QUESTION_NOTIFICATION_WORKER_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
        }
    }
}