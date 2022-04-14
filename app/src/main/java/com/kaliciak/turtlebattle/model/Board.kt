package com.kaliciak.turtlebattle.model

class Board(val width: Int,
            val height: Int,
            val turtles: List<Turtle>,
            val fps: Int) {

    val friction = 0.1f

    fun getState(): BoardState {
        return BoardState(height, width, turtles.map { turtle ->  turtle.getState()})
    }

    fun tick() {
        Physics.step(this)
    }
}