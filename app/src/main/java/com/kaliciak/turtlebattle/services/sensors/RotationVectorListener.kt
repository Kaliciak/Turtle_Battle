package com.kaliciak.turtlebattle.services.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class RotationVectorListener(private val delegate: RotationVectorListenerDelegate,
                             private val sensorManager: SensorManager
)
    : SensorEventListener {

    private val gravityReadings = FloatArray(3)
        @Synchronized get


    fun start() {
        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)?.also { gravity ->
            sensorManager.registerListener(
                this,
                gravity,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        gravityReadings[0] = event.values[0]
        gravityReadings[1] = event.values[1]
        gravityReadings[2] = event.values[2]
    }

    fun getReadings(): FloatArray = gravityReadings

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun stop() {
        sensorManager.unregisterListener(this)
    }
}