package com.kaliciak.turtlebattle.services

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kaliciak.turtlebattle.viewModel.GameViewModel

class RotationVectorListener(private val delegate: RotationVectorListenerDelegate,
                             private val sensorManager: SensorManager,
                             private val perSecond: Int)
    : SensorEventListener {
    var sensor: Sensor? = null
    var handler = Handler(Looper.getMainLooper())
    private var rotationClock: RotationClock? = null

    init {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        rotationClock = RotationClock(this)
        handler.postDelayed(rotationClock!!, (1000/perSecond).toLong())
    }

    override fun onSensorChanged(event: SensorEvent?) {
        delegate.didChange(event!!.values[1], event.values[0], event.values[2])
//        Log.d("listener", "${event!!.values[0]} ${event!!.values[1]} ${event!!.values[2]}")
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        Log.d("listener", "onAccuracyChanged")
    }

    inner class RotationClock(private val listener: SensorEventListener): Runnable {
        override fun run() {
            sensorManager.registerListener(listener, sensor, (1000/perSecond))
            handler.postDelayed(this, (1000/perSecond).toLong())
        }
    }
}