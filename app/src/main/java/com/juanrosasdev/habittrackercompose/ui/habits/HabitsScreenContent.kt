package com.juanrosasdev.habittrackercompose.ui.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.juanrosasdev.habittrackercompose.ui.habits.components.MonthlyHabitGrid
import com.juanrosasdev.habittrackercompose.ui.habits.components.ScreenHeader

@Composable
fun HabitsScreenContent(
    modifier: Modifier = Modifier,
    habits: List<HabitMonthRow>,
    monthTitle: String,
    todayLabel: String,
    days: List<Int>,
    todayDay: Int,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onToggle: (Int, Int, Boolean) -> Unit,
    onRequestDeleteHabit: (HabitMonthRow) -> Unit
) {
    Column(modifier) {
        ScreenHeader(
            title = "Habit Tracker · $monthTitle",
            subtitle = todayLabel,
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
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
