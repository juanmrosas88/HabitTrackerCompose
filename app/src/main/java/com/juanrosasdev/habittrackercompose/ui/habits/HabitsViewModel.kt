package com.juanrosasdev.habittrackercompose.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepository
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepositoryContract
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class HabitsViewModel(
    private val repository: HabitRepositoryContract
) : ViewModel()
{

    init {
        viewModelScope.launch {
            repository.ensureInitialHabits()
        }
    }

    private val today: LocalDate = LocalDate.now()
    val todayDayOfMonth: Int = LocalDate.now().dayOfMonth
    val todayLabel: String =
        today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            .replaceFirstChar { it.uppercase() } +
                ", ${today.dayOfMonth} de " +
                today.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { it.uppercase() }
    private val currentMonth = YearMonth.now()
    val monthTitle: String =
        currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val monthDays: List<Int> =
        (1..currentMonth.lengthOfMonth()).toList()

    val monthlyHabits: StateFlow<List<HabitMonthRow>> =
        repository.getMonthlyHabits(
            startDate = currentMonth.atDay(1).toString(),
            endDate = currentMonth.atEndOfMonth().toString()
        )
            .map { records ->
                records
                    .groupBy { it.habitId }
                    .map { (_, habitRecords) ->
                        val first = habitRecords.first()

                        val daysMap =
                            habitRecords
                                .filter { it.date != null }
                                .associate {
                                    LocalDate.parse(it.date!!).dayOfMonth to (it.isCompleted == true)
                                }

                        HabitMonthRow(
                            habitId = first.habitId,
                            name = first.name,
                            emoji = first.iconEmoji,
                            days = daysMap,
                            currentStreak = calculateCurrentStreak(
                                days = daysMap,
                                todayDay = todayDayOfMonth
                            )
                        )
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun addHabit(
        name: String,
        emoji: String
    ) {
        viewModelScope.launch {
            repository.insertHabit(name, emoji)
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
        }
    }

    fun onDayToggle(
        habitId: Int,
        day: Int,
        isCompleted: Boolean
    ) {
        val date = currentMonth.atDay(day).toString()
        viewModelScope.launch {
            repository.toggleHabit(
                habitId = habitId,
                date = date,
                isCompleted = isCompleted
            )
        }
    }

    private fun calculateCurrentStreak(
        days: Map<Int, Boolean>,
        todayDay: Int
    ): Int {
        var streak = 0

        for (day in todayDay downTo 1) {
            if (days[day] == true) {
                streak++
            } else {
                break
            }
        }
        return streak
    }
}
