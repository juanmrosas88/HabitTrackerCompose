package com.juanrosasdev.habittrackercompose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juanrosasdev.habittrackercompose.data.entities.HabitEntity
import com.juanrosasdev.habittrackercompose.data.entities.HabitRecordEntity
import kotlinx.coroutines.flow.Flow

// data/local/dao/HabitDao.kt
@Dao
interface HabitDao {

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT COUNT(*) FROM habits")
    suspend fun getHabitsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: HabitRecordEntity)

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
}

// Clase auxiliar para el resultado de la consulta
data class HabitWithStatus(
    val id: Int,
    val name: String,
    val iconEmoji: String,
    val isCompleted: Boolean
)