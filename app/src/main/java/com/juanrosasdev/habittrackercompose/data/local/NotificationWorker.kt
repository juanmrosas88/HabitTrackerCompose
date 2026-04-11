package com.juanrosasdev.habittrackercompose.data.local

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.util.*

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = HabitDatabase.getDatabase(applicationContext)
        val habitDao = database.habitDao()
        
        val habitsCount = habitDao.getHabitsCount()

        if (habitsCount > 0) {
            showNotification()
        }

        return Result.success()
    }

    private fun showNotification() {
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

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("¡No olvides tus hábitos! 🚀")
            .setContentText("Tienes hábitos pendientes para hoy. ¡Tú puedes!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
