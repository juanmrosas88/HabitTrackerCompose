package com.juanrosasdev.habittrackercompose.data.local

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.juanrosasdev.habittrackercompose.MainActivity
import java.time.LocalDate
import java.util.*

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = HabitDatabase.getDatabase(applicationContext)
        val habitDao = database.habitDao()
        
        val today = LocalDate.now().toString()
        val uncompletedCount = habitDao.getUncompletedHabitsCount(today)

        if (uncompletedCount > 0) {
            showNotification(uncompletedCount)
        }

        return Result.success()
    }

    private fun showNotification(count: Int) {
        val channelId = "habit_reminder_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios de Hábitos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para recordarte completar tus hábitos diarios"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val message = if (count == 1) {
            "Tienes 1 hábito pendiente por completar hoy. ¡No te rindas!"
        } else {
            "Tienes $count hábitos pendientes por completar hoy. ¡Vamos a por ellos!"
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("¡Hábitos pendientes! 🚀")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
