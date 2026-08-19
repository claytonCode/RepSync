package com.example.repsync.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class StepCounter(
    context: Context
) {
    private val sensorManager =
        context.getSystemService(
            Context.SENSOR_SERVICE
        ) as SensorManager

    private val stepSensor =
        sensorManager.getDefaultSensor(
            Sensor.TYPE_STEP_COUNTER
        )

    fun steps(): Flow<Int> = callbackFlow {

        if (stepSensor == null) {
            awaitClose()
            return@callbackFlow
        }

        val listener = object : SensorEventListener {

            override fun onSensorChanged(
                event: SensorEvent
            ) {
                val totalSteps = event.values[0].toInt()

                trySend(totalSteps)
            }

            override fun onAccuracyChanged(
                sensor: Sensor?,
                accuracy: Int
            ) {
            }
        }

        sensorManager.registerListener(
            listener,
            stepSensor,
            SensorManager.SENSOR_DELAY_GAME
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}