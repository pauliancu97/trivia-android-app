package com.example.triviaapp.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.triviaapp.R
import com.example.triviaapp.ui.models.Question
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_ID = 123
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.resources.getString(R.string.notification_channel_name)
            val description = context.resources.getString(R.string.notification_channel_description)
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setDescription(description)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showQuestionNotification(question: Question) {
        val title = context.resources.getString(
            R.string.category_and_difficulty,
            question.category.name,
            question.difficulty.name
        )
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.quiz)
            .setContentTitle(title)
            .setContentText(question.text)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(question.text)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addQuestionAction(question, context)
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    fun showAnswerNotification(answerData: AnswerData) {
        val text = when (answerData) {
            is AnswerData.AnswerBoolean -> {
                context.resources.getString(
                    if (answerData.isCorrect) R.string.correct else R.string.wrong
                )
            }
            is AnswerData.AnswerMultiple -> {
                if (answerData.isCorrect) {
                    context.resources.getString(R.string.correct)
                } else {
                    context.resources.getString(
                        R.string.wrong_with_answer,
                        answerData.correctAnswer
                    )
                }
            }
        }
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.quiz)
            .setContentTitle(context.resources.getString(R.string.app_title))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(text)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(context, 0, Intent(), 0)
            )
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}