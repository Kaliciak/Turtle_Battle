package com.kaliciak.turtlebattle.model

class Board(val height: Int, val width: Int, val turtles: List<Turtle>) {

    fun getState(): BoardState {
        return BoardState(height, width, turtles.map { turtle ->  turtle.getState()})
    }
}