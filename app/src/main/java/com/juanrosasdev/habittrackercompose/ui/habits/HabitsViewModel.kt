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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitsViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now().toString())
    val selectedDate = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val habitsUiState: StateFlow<List<HabitWithStatus>> =
        _selectedDate
            .flatMapLatest { date ->
                repository.getHabitsForDate(date)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    init {
        viewModelScope.launch {
            repository.ensureInitialHabits()
        }
    }

    fun onHabitChecked(habitId: Int, isChecked: Boolean) {
        viewModelScope.launch {
            repository.toggleHabit(
                habitId = habitId,
                date = _selectedDate.value,
                isCompleted = isChecked
            )
        }
    }
}
