package com.juanrosasdev.habittrackercompose.ui.habits.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juanrosasdev.habittrackercompose.ui.habits.HabitMonthRow
import kotlin.collections.forEach

@Composable
fun HabitNameColumn(
    habits: List<HabitMonthRow>
) {
    Column {
        Spacer(modifier = Modifier.height(40.dp))

        habits.forEach { habit ->
            Text(
                text = "${habit.emoji} ${habit.name}",
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}