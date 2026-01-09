package com.juanrosasdev.habittrackercompose.ui.habits.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juanrosasdev.habittrackercompose.ui.habits.HabitMonthRow


@Composable
fun HabitDayCell(
    habit: HabitMonthRow,
    day: Int,
    todayDay: Int,
    onToggle: (habitId: Int, day: Int, isCompleted: Boolean) -> Unit
) {
    val checked = habit.days[day] == true
    val enabled = day <= todayDay
    val isToday = day == todayDay

    Row(
        modifier = Modifier
            .height(40.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            enabled = enabled,
            onCheckedChange = { newValue ->
                onToggle(habit.habitId, day, newValue)
            }
        )

        if (isToday && checked) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                if (habit.currentStreak > 1) {
                    Text(
                        text = habit.currentStreak.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "🔥",
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

        }

    }
}
