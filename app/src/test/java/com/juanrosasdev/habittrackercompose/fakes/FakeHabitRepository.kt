package com.juanrosasdev.habittrackercompose.fakes

import com.juanrosasdev.habittrackercompose.data.local.dao.MonthlyHabitRecord
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepositoryContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
class FakeHabitRepository : HabitRepositoryContract {

    private val _records =
        MutableStateFlow<List<MonthlyHabitRecord>>(emptyList())

    fun setRecords(records: List<MonthlyHabitRecord>) {
        _records.value = records
    }

    override fun getMonthlyHabits(
        startDate: String,
        endDate: String
    ): Flow<List<MonthlyHabitRecord>> = _records

    override suspend fun insertHabit(name: String, emoji: String) {
        val newId = (_records.value.maxOfOrNull { it.habitId } ?: 0) + 1
        _records.value = _records.value + MonthlyHabitRecord(
            habitId = newId,
            name = name,
            iconEmoji = emoji,
            date = null,
            isCompleted = null
        )
    }

    override suspend fun deleteHabit(habitId: Int) {
        _records.value = _records.value.filterNot { it.habitId == habitId }
    }

    override suspend fun toggleHabit(
        habitId: Int,
        date: String,
        isCompleted: Boolean
    ) {
        _records.value = _records.value + MonthlyHabitRecord(
            habitId = habitId,
            name = _records.value.first { it.habitId == habitId }.name,
            iconEmoji = _records.value.first { it.habitId == habitId }.iconEmoji,
            date = date,
            isCompleted = isCompleted
        )
    }

    override suspend fun ensureInitialHabits() = Unit
}
