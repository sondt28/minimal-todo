package com.son.minimaltodo.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.son.minimaltodo.R
import com.son.minimaltodo.model.Task
import com.son.minimaltodo.ui.MainActivity.Companion.CHANNEL_ID
import com.son.minimaltodo.ui.reminder.ReminderFragmentArgs

fun NotificationManager.sendNotification(messageBody: String, context: Context, task: Task) {

    val pendingIntent: PendingIntent = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.reminderFragment)
        .setArguments(ReminderFragmentArgs(task).toBundle())
        .createPendingIntent()

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_check)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    notify(task.id, builder.build())
}