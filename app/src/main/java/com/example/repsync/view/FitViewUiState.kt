package com.example.repsync.view

import com.example.repsync.data.model.FitnessStat
import kotlin.time.Duration

data class FitViewUiState(
    val isRunning: Boolean = false,
    val elapsedTime: Duration = Duration.ZERO,
    val calories: Int = 0,
    val calorieGoal: Int = 240,
    val steps: Int = 0,
    val distanceKm: Float = 0f,
    val activeEnergyProgress: Float = 0f,
    val stats: List<FitnessStat> = emptyList()
)