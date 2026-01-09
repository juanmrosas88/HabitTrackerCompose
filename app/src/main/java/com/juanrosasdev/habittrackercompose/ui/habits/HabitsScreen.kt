package com.juanrosasdev.habittrackercompose.ui.habits

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juanrosasdev.habittrackercompose.ui.habits.dialogs.AddHabitDialog
import com.juanrosasdev.habittrackercompose.ui.habits.dialogs.ConfirmDeleteHabitDialog
import java.util.Locale

@Composable
fun HabitsScreen(viewModel: HabitsViewModel) {
    val habits by viewModel.monthlyHabits.collectAsStateWithLifecycle()

    var showAddHabitDialog by remember { mutableStateOf(false) }
    var habitToDelete by remember { mutableStateOf<HabitMonthRow?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddHabitDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar hábito")
            }
        }
    ) { padding ->

        HabitsScreenContent(
            modifier = Modifier.padding(padding),
            habits = habits,
            monthTitle = viewModel.monthTitle.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            },
            todayLabel = viewModel.todayLabel,
            days = viewModel.monthDays,
            todayDay = viewModel.todayDayOfMonth,
            onToggle = viewModel::onDayToggle,
            onRequestDeleteHabit = { habitToDelete = it }
        )

        if (showAddHabitDialog) {
            AddHabitDialog(
                onDismiss = { showAddHabitDialog = false },
                onConfirm = { name, emoji ->
                    viewModel.addHabit(name, emoji)
                    showAddHabitDialog = false
                }
            )
        }

        habitToDelete?.let { habit ->
            ConfirmDeleteHabitDialog(
                habitName = habit.name,
                onConfirm = {
                    viewModel.deleteHabit(habit.habitId)
                    habitToDelete = null
                },
                onDismiss = { habitToDelete = null }
            )
        }
    }
}
