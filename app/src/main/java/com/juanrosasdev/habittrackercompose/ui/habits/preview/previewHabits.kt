package com.juanrosasdev.habittrackercompose.ui.habits.preview

import com.juanrosasdev.habittrackercompose.ui.habits.HabitMonthRow

fun previewHabits(): List<HabitMonthRow> =
    listOf(
        HabitMonthRow(
            habitId = 1,
            name = "Ir al Gym",
            emoji = "💪",
            days = mapOf(1 to true, 2 to true, 3 to true, 4 to false, 5 to true),
            currentStreak = 1
        ),
        HabitMonthRow(
            habitId = 2,
            name = "Leer",
            emoji = "📚",
            days = mapOf(1 to true, 2 to false, 3 to true)
        ),
        HabitMonthRow(
            habitId = 3,
            name = "No Alcohol",
            emoji = "🍺",
            days = mapOf(1 to true, 2 to true, 3 to true, 4 to true, 5 to true),
            currentStreak = 5
        )
    )
