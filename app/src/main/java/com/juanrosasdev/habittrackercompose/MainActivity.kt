package com.juanrosasdev.habittrackercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.juanrosasdev.habittrackercompose.data.local.HabitDatabase
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepository
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsScreen
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsViewModel
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = HabitDatabase.getDatabase(this)
        val repository = HabitRepository(database.habitDao())

        val viewModel: HabitsViewModel by viewModels {
            HabitsViewModelFactory(repository)
        }

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                HabitsScreen(viewModel = viewModel)
            }
        }
    }
}
