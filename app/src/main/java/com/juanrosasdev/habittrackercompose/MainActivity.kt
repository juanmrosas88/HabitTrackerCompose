package com.juanrosasdev.habittrackercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juanrosasdev.habittrackercompose.data.local.HabitDatabase
import com.juanrosasdev.habittrackercompose.data.repository.HabitRepository
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsViewModel
import com.juanrosasdev.habittrackercompose.ui.habits.HabitsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = HabitDatabase.getDatabase(this)
        val repository = HabitRepository(database.habitDao())

        val viewModel: HabitsViewModel by viewModels {
            HabitsViewModelFactory(repository)
        }

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                HabitsScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel
) {
    // Estado que VIENE del ViewModel
    val habits by viewModel.habitsUiState.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .statusBarsPadding()
            ) {
                Text(
                    text = "Mi Planner",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = selectedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(
                items = habits,
                key = { it.id } // MUY importante
            ) { habit ->
                HabitRow(
                    habitName = habit.name,
                    emoji = habit.iconEmoji,
                    isChecked = habit.isCompleted,
                    onCheckedChange = { checked ->
                        viewModel.onHabitChecked(
                            habitId = habit.id,
                            isChecked = checked
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HabitRow(
    habitName: String,
    emoji: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
