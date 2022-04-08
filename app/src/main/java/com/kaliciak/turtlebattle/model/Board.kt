package com.kaliciak.turtlebattle.model

import android.util.Log
import kotlin.math.abs
import kotlin.math.sign

class Board(val width: Int,
            val height: Int,
            val turtles: List<Turtle>,
            val fps: Int) {
    private val friction = 0.2f

    fun getState(): BoardState {
        return BoardState(height, width, turtles.map { turtle ->  turtle.getState()})
    }

    fun tick() {
        moveTurtles()
    }

    private fun moveTurtles() {
        turtles.forEach(this::moveTurtle)
//        Log.d("turtle", "${turtles[3]}")
        for(turtle1 in turtles) {
            for(turtle2 in turtles) {
                if (turtle1 != turtle2) {
                    Physics.collide(turtle1, turtle2)
                }
            }
        }
    }

    private fun moveTurtle(turtle: Turtle) {
        turtle.x += (turtle.vX / fps)
        turtle.y += (turtle.vY / fps)

        // when trying to move the opposite direction
        val aX = turtle.forceX / turtle.mass
        val aY = turtle.forceY / turtle.mass
        turtle.vX += (aX / fps)
        turtle.vY += (aY / fps)
        if(sign(turtle.vX) != sign(aX)) {
            turtle.vX += 5 * (aX / fps)
        }
        if(sign(turtle.vY) != sign(aY)) {
            turtle.vY += 5 * (aY / fps)
        }

        checkBorders(turtle)

        // friction
        turtle.vX *= 1 - (friction / fps)
        turtle.vY *= 1 - (friction / fps)
    }

    private fun computeForces(turtle: Turtle) {
        var forceX = turtle.forceX
        if(friction * turtle.mass < abs(forceX)) {
            forceX -= sign(forceX) * (friction * turtle.mass).toFloat()
        } else {
            forceX = 0f
        }
        var forceY = turtle.forceY
        if(friction * turtle.mass < abs(forceY)) {
            forceY -= sign(forceY) * (friction * turtle.mass).toFloat()
        } else {
            forceY = 0f
        }

        turtle.forceX = forceX
        turtle.forceY = forceY
    }

    private fun checkBorders(turtle: Turtle) {
        if(turtle.x - turtle.r < 0) {
            turtle.x += 2 * abs(turtle.r - turtle.x)
            turtle.vX = -turtle.vX * 0.5f
        }
        if(turtle.x + turtle.r > width) {
            turtle.x -= 2 * abs(-width + turtle.x + turtle.r)
            turtle.vX = -turtle.vX * 0.5f
        }
        if(turtle.y - turtle.r < 0) {
            turtle.y += 2 * abs(turtle.r - turtle.y)
            turtle.vY = -turtle.vY * 0.5f
        }
        if(turtle.y + turtle.r > height) {
            turtle.y -= 2 * abs(-height + turtle.y + turtle.r)
            turtle.vY = -turtle.vY  * 0.5f
        }
    }
}