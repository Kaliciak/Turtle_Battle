package com.kaliciak.turtlebattle.model

class Board(val width: Int,
            val height: Int,
            var turtles: List<Turtle>,
            val fps: Int) {

    private val coastlineManager = CoastlineManager(width, height, fps)

    fun getState(): BoardState {
        return BoardState(height, width, turtles.map { turtle ->  turtle.getState()}, coastlineManager.coastline)
    }

    fun getCoastline(): Coastline {
        return coastlineManager.coastline
    }

    private fun getCoastX(lowerPoint: Point, upperPoint: Point, y: Float): Float {
        return lowerPoint.x - (lowerPoint.x - upperPoint.x) * (lowerPoint.y - y) / (lowerPoint.y - upperPoint.y)
    }

    private fun isOutsideCoastline(turtle: Turtle): Boolean {
        if(turtle.x < 0 || turtle.x > width) {
            return true
        }

        val coastline = coastlineManager.coastline
        var prevY = coastline.left[0].y
        var nextY = coastline.left[1].y
        var index = 1
        while(turtle.y !in nextY..prevY) {
            prevY = nextY
            index += 1
            nextY = coastline.left[index].y
        }

        if(turtle.x < getCoastX(coastline.left[index-1], coastline.left[index], turtle.y) ||
            turtle.x > getCoastX(coastline.right[index-1], coastline.right[index], turtle.y)) {
            return true
        }

        return false
    }

    private fun checkCoastline() {
        val newTurtles = turtles.toMutableList()
        turtles.forEach { turtle ->
            if(isOutsideCoastline(turtle))
                newTurtles.remove(turtle)
        }
        turtles = newTurtles
    }

    fun tick() {
        coastlineManager.step()
        Physics.step(this)
        checkCoastline()
    }
}