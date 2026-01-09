package com.juanrosasdev.habittrackercompose

import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juanrosasdev.habittrackercompose.data.local.HabitDatabase
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepository
import com.juanrosasdev.habittrackercompose.ui.habits.HabitMonthRow
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsViewModel
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsViewModelFactory
import java.util.Locale

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
fun HabitsScreenContent(
    habits: List<HabitMonthRow>,
    monthTitle: String,
    todayLabel: String,
    days: List<Int>,
    todayDay: Int,
    onToggle: (habitId: Int, day: Int, isCompleted: Boolean) -> Unit,
    onRequestDeleteHabit: (HabitMonthRow) -> Unit
) {
    Column {
        ScreenHeader(
            title = "Habit Tracker · $monthTitle",
            subtitle = todayLabel
        )

        MonthlyHabitGrid(
            habits = habits,
            days = days,
            todayDay = todayDay,
            onToggle = onToggle,
            onRequestDeleteHabit = onRequestDeleteHabit
        )
    }
}

@Composable
fun HabitsScreen(viewModel: HabitsViewModel) {
    val habits by viewModel.monthlyHabits.collectAsStateWithLifecycle()

    var showAddHabitDialog by remember { mutableStateOf(false) }
    var habitToDelete by remember { mutableStateOf<HabitMonthRow?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddHabitDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar hábito")
            }
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {
            HabitsScreenContent(
                habits = habits,
                monthTitle = viewModel.monthTitle.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                },
                todayLabel = viewModel.todayLabel,
                days = viewModel.monthDays,
                todayDay = viewModel.todayDayOfMonth,
                onToggle = viewModel::onDayToggle,
                onRequestDeleteHabit = { habit ->
                    habitToDelete = habit
                }
            )
        }

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
                onDismiss = {
                    habitToDelete = null
                }
            )
        }
    }
}


@Composable
fun ScreenHeader(
    title: String,
    subtitle: String
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MonthlyHabitGrid(
    habits: List<HabitMonthRow>,
    days: List<Int>,
    todayDay: Int,
    onToggle: (habitId: Int, day: Int, isCompleted: Boolean) -> Unit,
    onRequestDeleteHabit: (HabitMonthRow) -> Unit
) {
    Row {
        HabitNameColumn(habits)

        LazyRow {
            items(days) { day ->
                val backgroundColor = dayColumnBackground(day, todayDay)

                DayColumn(
                    day = day,
                    habits = habits,
                    todayDay = todayDay,
                    backgroundColor = backgroundColor,
                    onToggle = onToggle
                )
            }
            item{
                DeleteHabitColumn(
                    habits = habits,
                    onDelete = { habitId ->
                        habits.firstOrNull { it.habitId == habitId }?.let {
                            onRequestDeleteHabit(it)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ConfirmDeleteHabitDialog(
    habitName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar hábito") },
        text = {
            Text(
                "¿Seguro que querés eliminar el hábito \"$habitName\"?\n\n" +
                        "Esta acción no se puede deshacer."
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = "Eliminar",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun HabitNameColumn(
    habits: List<HabitMonthRow>
) {
    Column {
        Spacer(modifier = Modifier.height(40.dp))

        habits.forEach { habit ->
            Text(
                text = "${habit.emoji} ${habit.name}",
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun DeleteHabitColumn(
    habits: List<HabitMonthRow>,
    onDelete: (habitId: Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp)) // header vacío

        habits.forEach { habit ->
            IconButton(
                onClick = { onDelete(habit.habitId) },
                modifier = Modifier.height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar hábito",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun DayColumn(
    day: Int,
    habits: List<HabitMonthRow>,
    todayDay: Int,
    backgroundColor: Color,
    onToggle: (habitId: Int, day: Int, isCompleted: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.background(backgroundColor)
    ) {
        DayHeader(day)

        habits.forEach { habit ->
            HabitDayCell(
                habit = habit,
                day = day,
                todayDay = todayDay,
                onToggle = onToggle
            )
        }
    }
}


@Composable
fun DayHeader(day: Int) {
    Text(
        text = day.toString(),
        modifier = Modifier
            .height(40.dp)
            .padding(8.dp),
        style = MaterialTheme.typography.labelMedium
    )
}

@Composable
fun HabitDayCell(
    habit: HabitMonthRow,
    day: Int,
    todayDay: Int,
    onToggle: (habitId: Int, day: Int, isCompleted: Boolean) -> Unit
) {
    val checked = habit.days[day] == true
    val enabled = day <= todayDay
    val isToday = day == todayDay

    Row(
        modifier = Modifier
            .height(40.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            enabled = enabled,
            onCheckedChange = { newValue ->
                onToggle(habit.habitId, day, newValue)
            }
        )

        if (isToday && checked) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                if (habit.currentStreak > 1) {
                    Text(
                        text = habit.currentStreak.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "🔥",
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

        }

    }
}

@Composable
fun dayColumnBackground(
    day: Int,
    todayDay: Int
) = when {
    day == todayDay ->
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.35f)

    day % 2 == 0 ->
        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)

    else ->
        MaterialTheme.colorScheme.background
}


@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, emoji: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("🔥") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo hábito") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del hábito") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Emoji") }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onConfirm(name, emoji) }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun previewHabits(): List<HabitMonthRow> =
    listOf(
        HabitMonthRow(
            habitId = 1,
            name = "Ir al Gym",
            emoji = "💪",
            days = mapOf(
                1 to true,
                2 to true,
                3 to true,
                4 to false,
                5 to true
            ),
            currentStreak = 1

        ),
        HabitMonthRow(
            habitId = 2,
            name = "Leer",
            emoji = "📚",
            days = mapOf(
                1 to true,
                2 to false,
                3 to true
            )
        ),
        HabitMonthRow(
            habitId = 3,
            name = "No Alcohol",
            emoji = "🍺",
            days = mapOf(
                1 to true,
                2 to true,
                3 to true,
                4 to true,
                5 to true
            ),
            currentStreak = 5
        )
    )

@Preview(
    showBackground = true,
    widthDp = 2000,
    heightDp = 300
)
@Composable
fun HabitsScreenPreview() {
    MaterialTheme {
        HabitsScreenContent(
            habits = previewHabits(),
            monthTitle = "Septiembre",
            todayLabel = "Martes, 17 de Septiembre",
            days = (1..30).toList(),
            todayDay = 5,
            onToggle = { _, _, _ -> },
            onRequestDeleteHabit = {}
        )
    }
}


