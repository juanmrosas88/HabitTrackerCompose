package com.juanrosasdev.habittrackercompose.ui.habits.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsScreenContent
import com.juanrosasdev.habittrackercompose.ui.theme.HabitTrackerComposeTheme

@Preview(showBackground = true, widthDp = 1200, heightDp = 400)
@Composable
fun HabitsScreenLightPreview() {
    HabitTrackerComposeTheme(darkTheme = false) {
        HabitsScreenContent(
            habits = previewHabits(),
            monthTitle = "Septiembre",
            todayLabel = "Martes, 17 de Septiembre",
            days = (1..30).toList(),
            todayDay = 5,
            isDarkTheme = false,
            onThemeToggle = {},
            onToggle = { _, _, _ -> },
            onRequestDeleteHabit = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 400)
@Composable
fun HabitsScreenDarkPreview() {
    HabitTrackerComposeTheme(darkTheme = true) {
        HabitsScreenContent(
            habits = previewHabits(),
            monthTitle = "Septiembre",
            todayLabel = "Martes, 17 de Septiembre",
            days = (1..30).toList(),
            todayDay = 5,
            isDarkTheme = true,
            onThemeToggle = {},
            onToggle = { _, _, _ -> },
            onRequestDeleteHabit = {}
        )
    }
}
