package com.juanrosasdev.habittrackercompose.ui.habits

import com.juanrosasdev.habittrackercompose.data.local.dao.MonthlyHabitRecord
import com.juanrosasdev.habittrackercompose.fakes.FakeHabitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HabitsViewModelTest {

    private lateinit var fakeRepository: FakeHabitRepository
    private lateinit var viewModel: HabitsViewModel

    @Before
    fun setup() {
        fakeRepository = FakeHabitRepository()
        viewModel = HabitsViewModel(
            repository = fakeRepository // 👈 inyección fake
        )
    }

    @Test
    fun `addHabit adds habit to list`() = runTest {
        viewModel.addHabit("Leer", "📚")

        val habits = viewModel.monthlyHabits.first()

        assertEquals(1, habits.size)
        assertEquals("Leer", habits.first().name)
    }

    @Test
    fun `deleteHabit removes habit from list`() = runTest {
        fakeRepository.setRecords(
            listOf(
                MonthlyHabitRecord(
                    habitId = 1,
                    name = "Gym",
                    iconEmoji = "💪",
                    date = null,
                    isCompleted = null
                )
            )
        )

        viewModel.deleteHabit(1)

        val habits = viewModel.monthlyHabits.first()

        assertEquals(0, habits.size)
    }

}
