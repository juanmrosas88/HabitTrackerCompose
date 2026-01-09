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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    onToggle: (habitId: Int, day: Int, isCompleted: Boolean) -> Unit
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
            onToggle = onToggle
        )
    }
}


@Composable
fun HabitsScreen(viewModel: HabitsViewModel) {
    val habits by viewModel.monthlyHabits.collectAsStateWithLifecycle()

    HabitsScreenContent(
        habits = habits,
        monthTitle = viewModel.monthTitle.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        },
        todayLabel = viewModel.todayLabel,
        days = viewModel.monthDays,
        todayDay = viewModel.todayDayOfMonth,
        onToggle = viewModel::onDayToggle
    )
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
    onToggle: (habitId: Int, day: Int, isCompleted: Boolean) -> Unit
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
        }
    }
}

@Composable
fun HabitNameColumn(habits: List<HabitMonthRow>) {
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
    widthDp = 600,
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
            onToggle = { _, _, _ -> }
        )
    }
}


