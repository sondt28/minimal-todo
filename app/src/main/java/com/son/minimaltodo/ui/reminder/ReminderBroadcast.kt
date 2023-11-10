package com.son.minimaltodo.ui.reminder

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.son.minimaltodo.model.Task
import com.son.minimaltodo.util.sendNotification

class ReminderBroadcast : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val task = intent.getParcelableExtra("task") as Task?

        task?.let {
            notificationManager.sendNotification(task.title, context, task)
        }
    }
}