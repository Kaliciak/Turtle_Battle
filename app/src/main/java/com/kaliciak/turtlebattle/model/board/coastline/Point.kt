package com.kaliciak.turtlebattle.model.board.coastline

import kotlinx.serialization.Serializable

@Serializable
class Point(val x: Float = 0f, val y: Float = 0f) {
    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }
}