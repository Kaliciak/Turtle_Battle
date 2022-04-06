package com.kaliciak.turtlebattle.model

import android.hardware.SensorManager
import android.util.Log
import com.kaliciak.turtlebattle.services.RotationVectorListener
import com.kaliciak.turtlebattle.services.RotationVectorListenerDelegate

class Player(private val turtle: Turtle,
             private val sensorManager: SensorManager):
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
    val calX = 0.02f
    val calY = 0.02f

    init {
        turtle.forceX = 0f
        turtle.forceY = 0f
    }

    private var rotationClock = RotationVectorListener(this, sensorManager, 5)

    override fun didChange(valX: Float, valY: Float, valZ: Float) {
        this.valX = valX
        this.valY = valY
        this.valZ = valZ
    }

    fun tick() {
        turtle.forceX = (valX + calX) * 400
        turtle.forceY = (valY + calY) * 400
    }
}