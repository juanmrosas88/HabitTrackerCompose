package com.juanrosasdev.habittrackercompose.model

data class HabitState(
    val habit: Habit,
    var isCompleted: Boolean = false
)
