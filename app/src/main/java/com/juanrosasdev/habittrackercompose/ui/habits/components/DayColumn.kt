package com.juanrosasdev.habittrackercompose.ui.habits.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.juanrosasdev.habittrackercompose.ui.habits.HabitMonthRow
import kotlin.collections.forEach


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

