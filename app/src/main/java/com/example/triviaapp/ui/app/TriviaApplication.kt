package com.example.triviaapp.ui.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.triviaapp.ui.repositories.QuestionNotificationRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TriviaApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var factory: HiltWorkerFactory

    @Inject
    lateinit var questionNotificationRepository: QuestionNotificationRepository

    override fun onCreate() {
        super.onCreate()
        questionNotificationRepository.initialize()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(factory)
            .build()
}