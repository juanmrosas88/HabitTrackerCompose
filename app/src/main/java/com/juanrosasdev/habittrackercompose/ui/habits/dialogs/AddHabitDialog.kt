package com.juanrosasdev.habittrackercompose.ui.habits.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, emoji: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("🔥") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo hábito") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del hábito") }
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                TextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Emoji") }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onConfirm(name, emoji) }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
