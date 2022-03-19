package com.kaliciak.turtlebattle.model

class Turtle(var x: Double, var y: Double, var r: Double) {

    fun getState(): TurtleState {
        return TurtleState(x, y, r)
    }
}