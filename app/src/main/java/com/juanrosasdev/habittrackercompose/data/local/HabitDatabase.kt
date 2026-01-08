package com.juanrosasdev.habittrackercompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.juanrosasdev.habittrackercompose.data.entities.HabitEntity
import com.juanrosasdev.habittrackercompose.data.entities.HabitRecordEntity
import com.juanrosasdev.habittrackercompose.data.local.dao.HabitDao

@Database(
    entities = [HabitEntity::class, HabitRecordEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // Le decimos a Room que use nuestro convertidor
abstract class HabitDatabase : RoomDatabase() {

    // Aquí defines todos los DAOs que necesites
    abstract fun habitDao(): HabitDao

    // Patrón Singleton para evitar múltiples instancias de la DB abiertas
    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}