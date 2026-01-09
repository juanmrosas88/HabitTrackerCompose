package com.juanrosasdev.habittrackercompose.ui.habits.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsScreenContent

@Preview(showBackground = true, widthDp = 2000, heightDp = 300)
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
