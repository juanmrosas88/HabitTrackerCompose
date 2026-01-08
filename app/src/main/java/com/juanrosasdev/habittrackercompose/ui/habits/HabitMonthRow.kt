package com.juanrosasdev.habittrackercompose.ui.habits

data class HabitMonthRow(
    val habitId: Int,
    val name: String,
    val emoji: String,
    val days: Map<Int, Boolean>
)
