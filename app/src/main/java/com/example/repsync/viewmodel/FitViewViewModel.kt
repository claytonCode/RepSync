package com.example.repsync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repsync.data.model.FitnessStat
import com.example.repsync.repository.FitnessRepository
import com.example.repsync.util.constants.FitnessStatType
import com.example.repsync.view.FitViewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

@HiltViewModel
class FitViewModel @Inject constructor(
    private val repository: FitnessRepository
) : ViewModel() {

    private val _calorieGoal =  MutableStateFlow(500)

    private val _goalReached =  MutableStateFlow(false)
    val goalReached = _goalReached.asStateFlow()

    private var goalAlertShown = false

    private val workoutData =
        combine(
            repository.steps,
            repository.calories,
            repository.distanceKm
        ) { steps, calories, distance ->

            WorkoutData(
                steps = steps,
                calories = calories,
                distance = distance
            )
        }

    private val graphData =
        combine(
            repository.stepsHistory,
            repository.caloriesHistory,
            repository.distanceHistory
        ) { stepsHistory,
            caloriesHistory,
            distanceHistory ->

            GraphData(
                stepsHistory = stepsHistory,
                caloriesHistory = caloriesHistory,
                distanceHistory = distanceHistory
            )
        }

    val uiState =
        combine(
            repository.isRunning,
            repository.elapsedTime,
            workoutData,
            graphData,
            _calorieGoal
        ) { isRunning,
            elapsedTime,
            workout,
            graph,
            calorieGoal ->

            if (
                workout.calories >= calorieGoal &&
                !goalAlertShown
            ) {
                goalAlertShown = true
                _goalReached.value = true
            }

            val progress =
                (workout.calories / calorieGoal.toFloat())
                    .coerceIn(0f, 1f)

            FitViewUiState(
                isRunning = isRunning,
                elapsedTime = elapsedTime,
                calories = workout.calories,
                calorieGoal = calorieGoal,
                steps = workout.steps,
                distanceKm = workout.distance,
                activeEnergyProgress = progress,

                stats = listOf(
                    FitnessStat(
                        type = FitnessStatType.STEPS,
                        value = workout.steps.toString(),
                        unit = "",
                        graph = graph.stepsHistory
                    ),

                    FitnessStat(
                        type = FitnessStatType.DISTANCE,
                        value = String.format(
                            Locale.getDefault(),
                            "%.2f",
                            workout.distance
                        ),
                        unit = "KM",
                        graph = graph.distanceHistory
                    ),

                    FitnessStat(
                        type = FitnessStatType.CALORIES,
                        value = workout.calories.toString(),
                        unit = "",
                        graph = graph.caloriesHistory
                    )
                )
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FitViewUiState()
            )

    fun setCalorieGoal(goal: Int) {
        if (goal > 0) {
            _calorieGoal.value = goal
            goalAlertShown = false
            _goalReached.value = false
        }
    }
    fun resetWorkout() {
        repository.reset()
        goalAlertShown = false
        _goalReached.value = false
    }

    fun dismissGoalReached() {
        _goalReached.value = false
    }

    fun startWorkout() {
        repository.start()
    }

    fun pauseWorkout() {
        repository.pause()
    }

    fun finishWorkout() {
        viewModelScope.launch {
            repository.finish(
                calorieGoal = _calorieGoal.value
            )
        }
    }

    val workoutHistory =
        repository.workouts
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun clearWorkoutHistory() {
        viewModelScope.launch {
            repository.clearWorkoutHistory()
        }
    }

    private data class WorkoutData(
        val steps: Int,
        val calories: Int,
        val distance: Float
    )

    private data class GraphData(
        val stepsHistory: List<Float>,
        val caloriesHistory: List<Float>,
        val distanceHistory: List<Float>
    )
}