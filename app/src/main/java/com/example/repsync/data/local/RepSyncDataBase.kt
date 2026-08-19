package com.example.repsync.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [WorkoutEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RepSyncDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
}