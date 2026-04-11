package com.juanrosasdev.habittrackercompose

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.juanrosasdev.habittrackercompose.data.local.HabitDatabase
import com.juanrosasdev.habittrackercompose.data.local.NotificationWorker
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepository
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsScreen
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsViewModel
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsViewModelFactory
import com.juanrosasdev.habittrackercompose.ui.theme.HabitTrackerComposeTheme
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()
        scheduleNotification(targetHour = 21)

        val database = HabitDatabase.getDatabase(this)
        val repository = HabitRepository(database.habitDao())

        val viewModel: HabitsViewModel by viewModels {
            HabitsViewModelFactory(repository)
        }

        enableEdgeToEdge()

        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            HabitTrackerComposeTheme(darkTheme = isDarkTheme) {
                HabitsScreen(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = it }
                )
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun scheduleNotification(targetHour: Int) {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, 20)
            set(Calendar.SECOND, 0)
        }

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val initialDelay = dueDate.timeInMillis - currentDate.timeInMillis

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "habit_reminder_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
