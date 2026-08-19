package com.example.repsync.repository

import com.example.repsync.data.local.WorkoutEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

interface FitnessRepository {

    val isRunning: Flow<Boolean>
    val elapsedTime: Flow<Duration>
    val steps: Flow<Int>
    val calories: Flow<Int>
    val distanceKm: Flow<Float>
    val stepsHistory: StateFlow<List<Float>>
    val caloriesHistory: StateFlow<List<Float>>
    val distanceHistory: StateFlow<List<Float>>
    val workouts: Flow<List<WorkoutEntity>>
    suspend fun clearWorkoutHistory()
    fun start()
    fun pause()
    fun reset()
    suspend fun finish(calorieGoal: Int)

}