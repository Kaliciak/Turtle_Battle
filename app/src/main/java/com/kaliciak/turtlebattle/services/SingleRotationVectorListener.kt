package com.kaliciak.turtlebattle.services

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper

class SingleRotationVectorListener(private val delegate: RotationVectorListenerDelegate,
                                   private val sensorManager: SensorManager)
    : SensorEventListener {
    var sensor: Sensor? = null

    init {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    fun getData() {
        sensorManager.registerListener(this, sensor, 1000)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        delegate.didChange(event!!.values[1], event.values[0], event.values[2])
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        Log.d("listener", "onAccuracyChanged")
    }
}