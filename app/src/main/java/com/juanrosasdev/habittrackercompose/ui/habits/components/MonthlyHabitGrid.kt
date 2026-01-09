package com.juanrosasdev.habittrackercompose.ui.habits.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.juanrosasdev.habittrackercompose.ui.habits.HabitMonthRow

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