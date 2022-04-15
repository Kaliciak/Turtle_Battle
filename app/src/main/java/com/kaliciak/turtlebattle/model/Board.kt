package com.kaliciak.turtlebattle.model

class Board(val width: Int,
            val height: Int,
            val turtles: List<Turtle>,
            val fps: Int) {

    val friction = 0.1f
    // from down to up
    var leftBorder: MutableList<Point> = mutableListOf(
        Point(150f, height.toFloat()),
        Point(0f, 400f),
        Point(200f, 200f),
        Point(30f, 0f),
    )
    var rightBorder: MutableList<Point> = mutableListOf(
        Point(275f, height.toFloat()),
        Point(100f, 400f),
        Point(300f, 200f),
        Point(200f, 0f),
    )

    fun getState(): BoardState {
        return BoardState(height, width, turtles.map { turtle ->  turtle.getState()}, leftBorder, rightBorder)
    }

    fun tick() {
        Physics.step(this)
    }
}