package com.son.minimaltodo.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.son.minimaltodo.model.Task
import com.son.minimaltodo.ui.reminder.ReminderBroadcast
import java.time.ZoneId

object AlarmUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun setupAlarm(context: Context, task: Task) {
        if (!task.isNotify) {
            cancelAlarm(context, task.id)
            return
        }
        val intent = Intent(context, ReminderBroadcast::class.java).apply {
            putExtra("task", task)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmTimeMillis = task.date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (alarmTimeMillis > System.currentTimeMillis()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(context: Context, taskId: Int) {
        val intent = Intent(context, ReminderBroadcast::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, taskId, intent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}