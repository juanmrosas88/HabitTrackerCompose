package com.juanrosasdev.habittrackercompose.data.repository

import com.juanrosasdev.habittrackercompose.data.local.dao.MonthlyHabitRecord
import kotlinx.coroutines.flow.Flow

interface HabitRepositoryContract {

    fun getMonthlyHabits(
        startDate: String,
        endDate: String
    ): Flow<List<MonthlyHabitRecord>>

    suspend fun insertHabit(name: String, emoji: String)

    suspend fun deleteHabit(habitId: Int)

    suspend fun toggleHabit(
        habitId: Int,
        date: String,
        isCompleted: Boolean
    )

    suspend fun ensureInitialHabits()
}
