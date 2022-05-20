package com.kaliciak.turtlebattle.model.turtle

class Turtle(x: Float,
             y: Float,
             var r: Float,
             var mass: Float,
             var color: TurtleColor
) {

    var x: Float = x
        @Synchronized set
        @Synchronized get
    var y: Float = y
        @Synchronized set
        @Synchronized get
    var forceX = 0f
        @Synchronized set
        @Synchronized get
    var forceY = 0f
        @Synchronized set
        @Synchronized get
    var vX = 0f
        @Synchronized set
        @Synchronized get
    var vY = 0f
        @Synchronized set
        @Synchronized get

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

    @Synchronized
    fun getForcesData(): TurtleForcesData {
        return TurtleForcesData(forceX, forceY, color)
    }

    @Synchronized
    fun applyForcesData(forcesData: TurtleForcesData) {
        forceX = forcesData.forceX
        forceY = forcesData.forceY
    }

    @Synchronized
    fun getTurtleData(): TurtleData {
        return TurtleData(x, y, forceX, forceY, vX, vY, color)
    }

    @Synchronized
    fun applyTurtleData(turtleData: TurtleData) {
        x = turtleData.x
        y = turtleData.y
        forceX = turtleData.forceX
        forceY = turtleData.forceY
        vX = turtleData.vX
        vY = turtleData.vY
    }
}