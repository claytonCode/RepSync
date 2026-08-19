package com.example.repsync.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun timeAndEmit(
    emissionsPerSecond: Float,
    isRunning: StateFlow<Boolean>
): Flow<Duration> {

    return flow {

        var lastEmitTime = System.currentTimeMillis()
        emit(Duration.ZERO)

        while (true) {
            delay(
                (1000L / emissionsPerSecond)
                    .roundToLong()
                    .milliseconds
            )

            val currentTime = System.currentTimeMillis()

            if (isRunning.value) {
                val elapsedTime =
                    currentTime - lastEmitTime

                emit(elapsedTime.milliseconds)
            }

            lastEmitTime = currentTime
        }
    }
}