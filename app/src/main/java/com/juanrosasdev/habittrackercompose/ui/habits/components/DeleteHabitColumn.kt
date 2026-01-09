package com.juanrosasdev.habittrackercompose.ui.habits.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juanrosasdev.habittrackercompose.ui.habits.HabitMonthRow
import kotlin.collections.forEach


@Composable
fun DeleteHabitColumn(
    habits: List<HabitMonthRow>,
    onDelete: (habitId: Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp)) // header vacío

        habits.forEach { habit ->
            IconButton(
                onClick = { onDelete(habit.habitId) },
                modifier = Modifier.height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar hábito",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}