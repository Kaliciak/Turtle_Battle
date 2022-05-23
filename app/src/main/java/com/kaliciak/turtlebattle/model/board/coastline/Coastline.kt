package com.kaliciak.turtlebattle.model.board.coastline

import kotlinx.serialization.Serializable

// points from down to up
@Serializable
class Coastline(val left: List<Point> = listOf(), val right: List<Point> = listOf()) {
    override fun toString(): String {
        return "Coastline(\n left=$left,\n right=$right \n)"
    }
}