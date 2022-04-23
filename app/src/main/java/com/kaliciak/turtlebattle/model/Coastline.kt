package com.kaliciak.turtlebattle.model

// points from down to up
class Coastline(val left: List<Point> = listOf(), val right: List<Point> = listOf()) {
    override fun toString(): String {
        return "Coastline(\n left=$left,\n right=$right \n)"
    }
}