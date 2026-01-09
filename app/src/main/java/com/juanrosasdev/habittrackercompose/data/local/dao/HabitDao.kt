package com.juanrosasdev.habittrackercompose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juanrosasdev.habittrackercompose.data.entities.HabitEntity
import com.juanrosasdev.habittrackercompose.data.entities.HabitRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    // ---------- HABITS ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Int)

    @Query("SELECT COUNT(*) FROM habits")
    suspend fun getHabitsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    // ---------- RECORDS ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: HabitRecordEntity)

    // ---------- QUERIES ----------

    @Query(
        """
        SELECT h.id, h.name, h.iconEmoji, 
               COALESCE(r.isCompleted, 0) as isCompleted 
        FROM habits h 
        LEFT JOIN habit_records r 
            ON h.id = r.habitId AND r.date = :date
        """
    )
    fun getHabitsWithStatusForDate(date: String): Flow<List<HabitWithStatus>>

    @Query(
        """
        SELECT h.id as habitId,
               h.name as name,
               h.iconEmoji as iconEmoji,
               r.date as date,
               r.isCompleted as isCompleted
        FROM habits h
        LEFT JOIN habit_records r
            ON h.id = r.habitId
            AND r.date BETWEEN :startDate AND :endDate
        """
    )
    fun getMonthlyHabits(
        startDate: String,
        endDate: String
    ): Flow<List<MonthlyHabitRecord>>
}


// Clase auxiliar para el resultado de la consulta
data class HabitWithStatus(
    val id: Int,
    val name: String,
    val iconEmoji: String,
    val isCompleted: Boolean
)

data class MonthlyHabitRecord(
    val habitId: Int,
    val name: String,
    val iconEmoji: String,
    val date: String?,       // puede ser null
    val isCompleted: Boolean?
)