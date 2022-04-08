package com.kaliciak.turtlebattle.model

class Turtle(var x: Float,
             var y: Float,
             var r: Float,
             var mass: Int,
             var color: TurtleColor) {
    var forceX = 0f
    var forceY = 0f
    var vX = 50f
    var vY = 50f

    fun getState(): TurtleState {
        return TurtleState(x, y, r, color)
    }

    override fun toString(): String {
        return """$color 
            |r: $r, 
            |mass $mass, 
            |x: $x, 
            |y: $y,
            |vX: $vX, 
            |vY: $vY, 
            |fX: $forceX, 
            |fY: $forceY""".trimMargin()
    }
}