package com.juanrosasdev.habittrackercompose.ui.habits.dialogs

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDeleteHabitDialog(
    habitName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar hábito") },
        text = {
            Text(
                "¿Seguro que querés eliminar el hábito \"$habitName\"?\n\n" +
                        "Esta acción no se puede deshacer."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Eliminar",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
