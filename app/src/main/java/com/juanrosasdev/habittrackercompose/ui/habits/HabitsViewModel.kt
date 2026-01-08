package com.juanrosasdev.habittrackercompose.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanrosasdev.habittrackercompose.data.local.dao.HabitWithStatus
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class HabitsViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private val today: LocalDate = LocalDate.now()

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
                        HabitMonthRow(
                            habitId = first.habitId,
                            name = first.name,
                            emoji = first.iconEmoji,
                            days = habitRecords
                                .filter { it.date != null }
                                .associate {
                                    LocalDate.parse(it.date!!).dayOfMonth to (it.isCompleted == true)
                                }
                        )
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

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
}
