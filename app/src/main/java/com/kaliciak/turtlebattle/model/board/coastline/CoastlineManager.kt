package com.kaliciak.turtlebattle.model.board.coastline

import com.kaliciak.turtlebattle.model.turtle.Turtle
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class CoastlineManager(private val width: Int, private val height: Int, private val fps: Int) {
    var coastline: Coastline = Coastline()
        private set

    init {
        val leftCoast = mutableListOf(
            Point(0f, height.toFloat()),
            Point(0f, 0f)
        )

        val rightCoast = mutableListOf(
            Point(width.toFloat(), height.toFloat()),
            Point(width.toFloat(), 0f)
        )

        coastline = Coastline(leftCoast, rightCoast)
    }

    var speed = height.toFloat() * (1f/10f)
        private set
    private val maxSpeed = height.toFloat() * (1f/2f)
    private val acceleration =  height.toFloat() * (1f/0100f)

    private var minLandWidth = width * (2f/3f)
    private val limLandWidth = width * (1f/4f)
    private val landWidthAcc = width * (1f/200f)

    private var maxDerivation = width * (1f/10f)
    private val limDerivation = width * (1f/3f)
    private val derivationAcc = width * (1f/200f)

    private val vSpace = height * (1f/5f)

    fun applyCoastline(coastline: Coastline, speed: Float) {
        this.coastline = coastline
        this.speed = speed
    }

    private fun reduceCoast(coast: List<Point>): List<Point> {
        val result = coast.toMutableList()
        while(result.size > 1 && result[1].y >= height) {
            result.removeAt(0)
        }
        return result
    }

    private fun getRandomShift(x: Float): Float {
        var rand = Random.nextFloat()
        rand -= 0.5f
        rand *= 2
        rand *= maxDerivation
        return x + rand
    }

    private fun expandCoastline(coastline: Coastline): Coastline {
        val leftCoast = coastline.left.toMutableList()
        val rightCoast = coastline.right.toMutableList()

        while(leftCoast.last().y > -height) {
            val prevHeight = leftCoast.last().y
            val newHeight = prevHeight - vSpace
            val prevLeftX = leftCoast.last().x
            val prevRightX = rightCoast.last().x
            var leftX = 0f
            var rightX = 0f

            while(rightX - leftX < minLandWidth
                || leftX < 0 || rightX > width) {
                leftX = getRandomShift(prevLeftX)
                rightX = getRandomShift(prevRightX)
            }

            leftCoast.add(Point(leftX, newHeight))
            rightCoast.add(Point(rightX, newHeight))
        }

        return Coastline(leftCoast, rightCoast)
    }

    private fun moveCoast(coast: List<Point>): List<Point> {
        return coast.map { point -> Point(point.x, point.y + speed/fps) }
    }

    fun step() {
        var leftCoast = coastline.left
        var rightCoast = coastline.right

        leftCoast = moveCoast(leftCoast)
        rightCoast = moveCoast(rightCoast)

        leftCoast = reduceCoast(leftCoast)
        rightCoast = reduceCoast(rightCoast)
        coastline = expandCoastline(Coastline(leftCoast, rightCoast))

        speed = min(maxSpeed, speed + acceleration/fps)
        minLandWidth = max(limLandWidth, minLandWidth - landWidthAcc/fps)
        maxDerivation = min(limDerivation, maxDerivation + derivationAcc/fps)
    }

    private fun getCoastX(lowerPoint: Point, upperPoint: Point, y: Float): Float {
        return lowerPoint.x - (lowerPoint.x - upperPoint.x) * (lowerPoint.y - y) / (lowerPoint.y - upperPoint.y)
    }

    fun isOutsideCoastline(turtle: Turtle): Boolean {
        if(turtle.x < 0 || turtle.x > width) {
            return true
        }

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
}