package com.kaliciak.turtlebattle.services.sensors

interface RotationVectorListenerDelegate {
    fun didChange(valX: Float, valY: Float, valZ: Float)
}