package com.example.repsync.repository

import com.example.repsync.data.local.WorkoutDao
import com.example.repsync.data.local.WorkoutEntity
import com.example.repsync.data.sensor.StepCounter
import com.example.repsync.data.timeAndEmit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration

class FitnessRepositoryImpl(
    private val stepCounter: StepCounter,
    private val workoutDao: WorkoutDao
) : FitnessRepository {

    companion object {
        private const val STRIDE_LENGTH_METERS = 0.75f
        private const val KCAL_PER_STEP = 0.032f

    }

    private var workoutStartTime: Long? = null
    private val scope =
        CoroutineScope(Dispatchers.Default)

    private var sensorJob: Job? = null
    private var timerJob: Job? = null

    private var sensorSteps = 0
    private var startingSteps = 0

    private var hasStartedWorkout = false
    private val _isRunning =
        MutableStateFlow(false)

    override val isRunning =
        _isRunning.asStateFlow()

    private val _elapsedTime =
        MutableStateFlow(Duration.ZERO)

    override val elapsedTime =
        _elapsedTime.asStateFlow()

    private val _steps =
        MutableStateFlow(0)

    override val steps =
        _steps.asStateFlow()

    private val _stepsHistory =
        MutableStateFlow<List<Float>>(emptyList())

    override val stepsHistory =
        _stepsHistory.asStateFlow()

    private val _caloriesHistory =
        MutableStateFlow<List<Float>>(emptyList())

    override val caloriesHistory =
        _caloriesHistory.asStateFlow()

    private val _distanceHistory =
        MutableStateFlow<List<Float>>(emptyList())

    override val distanceHistory =
        _distanceHistory.asStateFlow()

    init {
        observeSteps()
    }

    private fun observeSteps() {

        sensorJob = scope.launch {

            stepCounter
                .steps()
                .collectLatest { totalSteps ->

                    sensorSteps = totalSteps

                    if (_isRunning.value) {

                        val workoutSteps =
                            (totalSteps - startingSteps)
                                .coerceAtLeast(0)

                        _steps.value = workoutSteps

                        updateHistory(workoutSteps)
                    }
                }
        }
    }

    private fun updateHistory(
        workoutSteps: Int
    ) {

        val calories =
            workoutSteps * KCAL_PER_STEP

        val distance =
            (workoutSteps * STRIDE_LENGTH_METERS) / 1000f

        _stepsHistory.value =
            (_stepsHistory.value + workoutSteps.toFloat())
                .takeLast(30)

        _caloriesHistory.value =
            (_caloriesHistory.value + calories)
                .takeLast(30)

        _distanceHistory.value =
            (_distanceHistory.value + distance)
                .takeLast(30)
    }

    private fun startTimer() {

        timerJob?.cancel()

        timerJob = scope.launch {
            timeAndEmit(
                emissionsPerSecond = 100f,
                isRunning = _isRunning
            ).collectLatest { elapsed ->

                if (_isRunning.value) {
                    _elapsedTime.value += elapsed
                }
            }
        }
    }

    override fun start() {
        if (_isRunning.value) {
            return
        }
        if (workoutStartTime == null) {

            workoutStartTime =
                System.currentTimeMillis()

            startingSteps =
                sensorSteps
        }
        _isRunning.value = true

        startTimer()
    }

    override fun pause() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    override fun reset() {

        _isRunning.value = false

        timerJob?.cancel()

        _elapsedTime.value = Duration.ZERO

        _steps.value = 0

        workoutStartTime = null

        startingSteps = sensorSteps

        _stepsHistory.value = emptyList()

        _caloriesHistory.value = emptyList()

        _distanceHistory.value = emptyList()

    }

    override val calories: Flow<Int> =
        steps.map { stepCount ->
            (stepCount * KCAL_PER_STEP)
                .toInt()
        }

    override val distanceKm: Flow<Float> =
        steps.map { stepCount ->
            (stepCount * STRIDE_LENGTH_METERS) /
                    1000f
        }

    override suspend fun finish(
        calorieGoal: Int
    ) {
        val startTime = workoutStartTime ?: return
        val endTime = System.currentTimeMillis()
        val currentSteps = _steps.value
        val currentCalories =
            (currentSteps * KCAL_PER_STEP)
                .toInt()
        val currentDistance =
            (currentSteps * STRIDE_LENGTH_METERS) / 1000f

        workoutDao.insertWorkout(
            WorkoutEntity(
                startTime = startTime,
                endTime = endTime,
                durationMillis = elapsedTime.value
                    .inWholeMilliseconds,
                steps = currentSteps,
                calories = currentCalories,
                distanceKm = currentDistance,
                calorieGoal = calorieGoal
            )
        )

        _isRunning.value = false
        timerJob?.cancel()
        workoutStartTime = null
    }

    override val workouts: Flow<List<WorkoutEntity>> =
        workoutDao.observeWorkouts()

    override suspend fun clearWorkoutHistory() {
        workoutDao.clearWorkouts()
    }

}