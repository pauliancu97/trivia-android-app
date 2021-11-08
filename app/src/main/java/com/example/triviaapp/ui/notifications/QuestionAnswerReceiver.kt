package com.example.triviaapp.ui.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.ui.text.toLowerCase
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.triviaapp.R
import com.example.triviaapp.ui.models.Question
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ANSWER_NOTIFICATION_ACTION = "com.example.triviaapp.ANSWER_QUESTION"
private const val ANSWER_EXTRA = "ANSWER_EXTRA"
private const val ANSWER_STRING = "ANSWER_STRING"
private const val REQ_CODE = 512

private fun getBooleanAnswerIntent(
    userAnswer: Boolean,
    question: Question.QuestionBoolean,
    context: Context
): PendingIntent {
    val intent = Intent(context, QuestionAnswerReceiver::class.java).apply {
        action = ANSWER_NOTIFICATION_ACTION
        putExtra(
            ANSWER_EXTRA,
            QuestionNotificationData.QuestionBoolean(
                userAnswer = userAnswer,
                correctAnswer = question.correctAnswer
            )
        )
    }
    return PendingIntent.getBroadcast(
        context,
        REQ_CODE,
        intent,
        0
    )
}

private fun NotificationCompat.Builder.addQuestionBooleanAction(
    question: Question.QuestionBoolean,
    context: Context
): NotificationCompat.Builder {
    addAction(
        R.drawable.done,
        context.resources.getString(R.string.true_label),
        getBooleanAnswerIntent(true, question, context)
    )
    addAction(
        R.drawable.close,
        context.resources.getString(R.string.false_label),
        getBooleanAnswerIntent(false, question, context)
    )
    return this
}

private fun NotificationCompat.Builder.addQuestionMultipleAction(
    question: Question.QuestionMultiple,
    context: Context
): NotificationCompat.Builder {
    val remoteInput = RemoteInput.Builder(ANSWER_STRING)
        .setLabel(context.resources.getString(R.string.answer))
        .build()
    val intent = Intent(context, QuestionAnswerReceiver::class.java).apply {
        action = ANSWER_NOTIFICATION_ACTION
        putExtra(ANSWER_EXTRA, QuestionNotificationData.QuestionMultiple(question.correctAnswer))
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        REQ_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val action = NotificationCompat.Action.Builder(
        R.drawable.send,
        context.getString(R.string.answer),
        pendingIntent
    )
        .addRemoteInput(remoteInput)
        .build()
    addAction(action)
    return this
}

fun NotificationCompat.Builder.addQuestionAction(
    question: Question,
    context: Context
): NotificationCompat.Builder =
    when (question) {
        is Question.QuestionBoolean -> addQuestionBooleanAction(question, context)
        is Question.QuestionMultiple -> addQuestionMultipleAction(question, context)
    }

private fun getAnswer(intent: Intent) =
    RemoteInput.getResultsFromIntent(intent)
        .getCharSequence(ANSWER_STRING)?.toString()

@AndroidEntryPoint
class QuestionAnswerReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationsHelper: NotificationsHelper

    override fun onReceive(context: Context, intent: Intent) {
        val answerData = when (val questionData = intent.getParcelableExtra<QuestionNotificationData>(ANSWER_EXTRA)) {
            is QuestionNotificationData.QuestionBoolean -> AnswerData.AnswerBoolean(
                questionData.correctAnswer == questionData.userAnswer
            )
            is QuestionNotificationData.QuestionMultiple -> {
                val userAnswer = getAnswer(intent)
                AnswerData.AnswerMultiple(
                    isCorrect = questionData.correctAnswer.lowercase() == userAnswer?.lowercase(),
                    correctAnswer = questionData.correctAnswer
                )
            }
            else -> null
        }
        answerData?.let {
            notificationsHelper.showAnswerNotification(it)
        }
    }

}