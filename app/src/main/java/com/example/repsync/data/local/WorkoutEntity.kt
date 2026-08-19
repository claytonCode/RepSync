package com.example.repsync.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val startTime: Long,
    val endTime: Long,
    val durationMillis: Long,
    val steps: Int,
    val calories: Int,
    val distanceKm: Float,
    val calorieGoal: Int
)