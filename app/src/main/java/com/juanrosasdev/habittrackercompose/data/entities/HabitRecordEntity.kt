package com.juanrosasdev.habittrackercompose.data.entities

import androidx.room.Entity

@Entity(
    tableName = "habit_records",
    primaryKeys = ["habitId", "date"] // Un registro por hábito por día
)
data class HabitRecordEntity(
    val habitId: Int,
    val date: String, // Formato "yyyy-MM-dd"
    val isCompleted: Boolean
)