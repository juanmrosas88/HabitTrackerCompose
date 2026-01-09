package com.juanrosasdev.habittrackercompose.ui.habits.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DayHeader(day: Int) {
    Text(
        text = day.toString(),
        modifier = Modifier
            .height(40.dp)
            .padding(8.dp),
        style = MaterialTheme.typography.labelMedium
    )
}