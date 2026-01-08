package com.juanrosasdev.habittrackercompose.data.initial

import com.juanrosasdev.habittrackercompose.data.entities.HabitEntity

class InitialHabit {

    val initialHabits = listOf(
        HabitEntity(name = "Ir al Gym", iconEmoji = "💪"),
        HabitEntity(name = "Leer", iconEmoji = "📚"),
        HabitEntity(name = "Control de Redes Sociales", iconEmoji = "📵"),
        HabitEntity(name = "No Alcohol", iconEmoji = "🍺")
    )
}