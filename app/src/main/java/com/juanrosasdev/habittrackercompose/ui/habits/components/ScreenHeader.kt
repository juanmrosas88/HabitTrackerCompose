package com.juanrosasdev.habittrackercompose.ui.habits.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenHeader(
    title: String,
    subtitle: String,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.headlineMedium)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isDarkTheme) "🌙" else "☀️",
                modifier = Modifier.padding(end = 8.dp)
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = onThemeToggle
            )
        }
    }
}
