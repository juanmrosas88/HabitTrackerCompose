package com.juanrosasdev.habittrackercompose.ui.habits.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun dayColumnBackground(
    day: Int,
    todayDay: Int
) = when {
    day == todayDay ->
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.35f)

    day % 2 == 0 ->
        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)

    else ->
        MaterialTheme.colorScheme.background
}