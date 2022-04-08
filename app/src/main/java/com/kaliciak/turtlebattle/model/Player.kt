package com.kaliciak.turtlebattle.model

import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kaliciak.turtlebattle.services.RotationVectorListener
import com.kaliciak.turtlebattle.services.RotationVectorListenerDelegate


class Player(val turtle: Turtle,
             private val sensorManager: SensorManager,
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
    var handler = Handler(Looper.getMainLooper())
    private var rotationVectorListener = RotationVectorListener(this, sensorManager)

    init {
        turtle.vX = 0f
        turtle.vY = 0f
        turtle.forceX = 0f
        turtle.forceY = 0f
        rotationVectorListener.start()
    }

    override fun didChange(valX: Float, valY: Float, valZ: Float) {
        this.valX = valX - calibrationData.x
        this.valY = valY - calibrationData.y
        this.valZ = valZ - calibrationData.z
//        Log.d("change", "$valX ${this.valX} $valY ${this.valY}")
    }

    fun tick() {
        turtle.forceX = (valX) * 500
        turtle.forceY = (-valY) * 500
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
    }
}