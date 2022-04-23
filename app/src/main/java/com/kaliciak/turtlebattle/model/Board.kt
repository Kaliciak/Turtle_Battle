package com.kaliciak.turtlebattle.model

class Board(val width: Int,
            val height: Int,
            val turtles: List<Turtle>,
            val fps: Int) {

    private val coastlineManager = CoastlineManager(width, height, fps)

    fun getState(): BoardState {
        return BoardState(height, width, turtles.map { turtle ->  turtle.getState()}, coastlineManager.coastline)
    }

    fun getCoastline(): Coastline {
        return coastlineManager.coastline
    }

    fun tick() {
        coastlineManager.step()
        Physics.step(this)
    }
}