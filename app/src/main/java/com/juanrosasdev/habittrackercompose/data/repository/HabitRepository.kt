package com.juanrosasdev.habittrackercompose.data.repository

import com.juanrosasdev.habittrackercompose.data.entities.HabitEntity
import com.juanrosasdev.habittrackercompose.data.entities.HabitRecordEntity
import com.juanrosasdev.habittrackercompose.data.local.dao.HabitDao

class HabitRepository(private val habitDao: HabitDao) {

    fun getHabitsForDate(date: String) =
        habitDao.getHabitsWithStatusForDate(date)

    fun getMonthlyHabits(
        startDate: String,
        endDate: String
    ) =
        habitDao.getMonthlyHabits(startDate, endDate)

    suspend fun toggleHabit(
        habitId: Int,
        date: String,
        isCompleted: Boolean
    ) {
        habitDao.insertRecord(
            HabitRecordEntity(habitId, date, isCompleted)
        )
    }

    suspend fun ensureInitialHabits() {
        val count = habitDao.getHabitsCount()
        if (count == 0) {
            habitDao.insertHabits(initialHabits)
        }
    }

    private val initialHabits = listOf(
        HabitEntity(name = "Ir al Gym", iconEmoji = "💪"),
        HabitEntity(name = "Leer", iconEmoji = "📚"),
        HabitEntity(name = "Control de Redes Sociales", iconEmoji = "📵"),
        HabitEntity(name = "No Alcohol", iconEmoji = "🍺")
    )
}