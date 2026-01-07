package com.juanrosasdev.habittrackercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juanrosasdev.habittrackercompose.model.Habit
import com.juanrosasdev.habittrackercompose.model.HabitState
import com.juanrosasdev.habittrackercompose.ui.theme.HabitTrackerComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            HabitScreenScreenPrototype()
        }
    }
}

// PROTOTIPO RÁPIDO (NO PARA PRODUCCIÓN)
@Composable
fun HabitScreenScreenPrototype() {
    // Lista mutable que Compose observa
    val habitsListState = remember {
        mutableStateListOf(
            HabitState(Habit(1, "Ir al Gym", "💪")),
            HabitState(Habit(2, "Leer", "📚")),
            HabitState(Habit(2, "Control de Redes Sociales", "📵")),
            HabitState(Habit(2, "No Alcohol", "🍺"))
        )
    }

    LazyColumn {
        items(habitsListState.size) { index ->
            val habitState = habitsListState[index]
            HabitRow(
                habitName = habitState.habit.name,
                emoji = habitState.habit.iconEmoji,
                isChecked = habitState.isCompleted,
                onCheckedChange = { checked ->
                    // Actualizamos la lista reactiva
                    habitsListState[index] = habitState.copy(isCompleted = checked)
                }
            )
        }
    }
}

@Composable
fun HabitRow(
    habitName: String,
    emoji: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit // "State Hoisting" (ver punto 3)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, modifier = Modifier.padding(end = 8.dp))
        Text(text = habitName, modifier = Modifier.weight(1f))
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HabitTrackerPreview() {
    HabitScreenScreenPrototype()
}