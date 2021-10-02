package com.example.triviaapp.ui.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.triviaapp.ui.repositories.TriviaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltWorker
class UpdateCategoriesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val triviaRepository: TriviaRepository
): CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result =
        try {
            triviaRepository.updateCategories()
            Timber.d("Update categories worker success")
            Result.success()
        } catch (ex: Throwable) {
            Timber.d("Update categories worker failure")
            Result.retry()
        }
}

class UpdateCategoriesWorkerRequester @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PERIOD_VALUE = 30L
        private val PERIOD_UM = TimeUnit.MINUTES
        private const val WORK_NAME = "update_categories_work"
    }

    fun enqueue() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = PeriodicWorkRequestBuilder<UpdateCategoriesWorker>(PERIOD_VALUE, PERIOD_UM)
            .setConstraints(constraints)
            .setInitialDelay(PERIOD_VALUE, PERIOD_UM)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}