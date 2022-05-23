package com.kaliciak.turtlebattle.model

import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import com.kaliciak.turtlebattle.model.turtle.Turtle
import com.kaliciak.turtlebattle.services.sensors.RotationVectorListener
import com.kaliciak.turtlebattle.services.sensors.RotationVectorListenerDelegate


class Player(val turtle: Turtle,
             sensorManager: SensorManager,
             private val calibrationData: CalibrationData):
    RotationVectorListenerDelegate {

    var valX: Float = 0f
        @Synchronized get
        @Synchronized private set
    var valY: Float = 0f
        @Synchronized get
        @Synchronized private set
    var valZ: Float = 0f
        @Synchronized get
        @Synchronized private set
    private var rotationVectorListener = RotationVectorListener(this, sensorManager)

    private val forceMultiplier = 100

    // Updates per second
    private val ups = 30
    private var handler = Handler(Looper.getMainLooper())
    private val updateClock = UpdateClock()
    private var stopped = false

    init {
        turtle.vX = 0f
        turtle.vY = 0f
        turtle.forceX = 0f
        turtle.forceY = 0f
        rotationVectorListener.start()
        handler.postDelayed(updateClock, ((1000)/ups).toLong())
    }

    override fun didChange(valX: Float, valY: Float, valZ: Float) {
        this.valX = valX - calibrationData.x
        this.valY = valY - calibrationData.y
        this.valZ = valZ - calibrationData.z
    }

    fun tick() {
        turtle.forceX = (-valX) * forceMultiplier
        turtle.forceY = (valY) * forceMultiplier
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        rotationVectorListener.stop()
        stopped = true
    }

    inner class UpdateClock: Runnable {
        override fun run() {
            val readings = rotationVectorListener.getReadings()
            didChange(readings[0], readings[1], readings[2])
            if(!stopped) {
                handler.postDelayed(this, ((1000)/ups).toLong())
            }
        }
    }
}