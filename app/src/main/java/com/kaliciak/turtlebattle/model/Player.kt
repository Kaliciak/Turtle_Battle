package com.kaliciak.turtlebattle.model

import android.hardware.SensorManager
import android.util.Log
import com.kaliciak.turtlebattle.services.RotationVectorListener
import com.kaliciak.turtlebattle.services.RotationVectorListenerDelegate
import kotlin.math.log

class Player(private val turtle: Turtle,
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

    init {
        turtle.vX = 0f
        turtle.vY = 0f
        turtle.forceX = 0f
        turtle.forceY = 0f
    }

    private var rotationVectorListener = RotationVectorListener(this, sensorManager, 5)

    override fun didChange(valX: Float, valY: Float, valZ: Float) {
        this.valX = valX - calibrationData.x
        this.valY = valY - calibrationData.y
        this.valZ = valZ - calibrationData.z
        Log.d("change", "$valX ${this.valX} $valY ${this.valY}")
    }

    fun tick() {
        turtle.forceX = (valX) * 400
        turtle.forceY = (valY) * 400
    }

    fun stop() {
        rotationVectorListener.stop()
    }
}