package com.juanrosasdev.habittrackercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juanrosasdev.habittrackercompose.data.local.HabitDatabase
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepository
import com.juanrosasdev.habittrackercompose.ui.habits.HabitMonthRow
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

@Composable
fun HabitsScreen(viewModel: HabitsViewModel) {
    val habits by viewModel.monthlyHabits.collectAsStateWithLifecycle()

    Column {
        Text(
            text = "Habit Tracker · ${viewModel.monthTitle}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = viewModel.todayLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        MonthlyHabitGrid(
            habits = habits,
            days = viewModel.monthDays,
            onToggle = viewModel::onDayToggle
        )
    }
}

@Composable
fun MonthlyHabitGrid(
    habits: List<HabitMonthRow>,
    days: List<Int>,
    onToggle: (habitId: Int, day: Int, isCompleted: Boolean) -> Unit
) {
    Row {
        // Columna fija: nombres de hábitos
        Column {
            Spacer(modifier = Modifier.height(40.dp)) // header vacío
            habits.forEach { habit ->
                Text(
                    text = "${habit.emoji} ${habit.name}",
                    modifier = Modifier
                        .height(40.dp)
                        .padding(horizontal = 8.dp)
                )
            }
        }

        // Grilla desplazable
        LazyRow {
            items(days) { day ->
                Column {
                    // Header día
                    Text(
                        text = day.toString(),
                        modifier = Modifier
                            .height(40.dp)
                            .padding(8.dp)
                    )

                    habits.forEach { habit ->
                        val checked = habit.days[day] == true
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { newValue ->
                                onToggle(habit.habitId, day, newValue)
                            },
                            modifier = Modifier.height(40.dp)
                        )

                    }
                }
            }
        }
    }
}

