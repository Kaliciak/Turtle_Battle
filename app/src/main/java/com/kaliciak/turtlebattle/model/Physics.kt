package com.kaliciak.turtlebattle.model

import android.util.Log
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

class Physics {
    companion object {
        val friction = 0.1f
        fun step(board: Board) {
            val turtles = board.turtles
            turtles.forEach{turtle -> processTurtle(turtle, board)}
        }

        private fun processTurtle(turtle: Turtle, board: Board) {
            moveTurtle(turtle, board.fps)
            applyFriction(turtle, board)
            applyForces(turtle, board.fps)
            while (!hasValidPosition(turtle, board)) {
//                Log.d("process", "$turtle")
                bounceOffBorders(turtle, board)
                board.turtles.forEach { turtle2 ->
                    if(turtle != turtle2) {
                        collide(turtle, turtle2)
                     }
                }
            }
        }

        private fun moveTurtle(turtle: Turtle, fps: Int) {
            turtle.x += (turtle.vX / fps)
            turtle.y += (turtle.vY / fps)
        }

        private fun applyFriction(turtle: Turtle, board: Board) {
            turtle.vX *= 1 - (friction / board.fps)
            turtle.vY *= 1 - (friction / board.fps)
        }

        private fun applyForces(turtle: Turtle, fps: Int) {
            val aX = turtle.forceX / turtle.mass
            val aY = turtle.forceY / turtle.mass
            turtle.vX += (aX / fps)
            turtle.vY += (aY / fps)
            // when changing the direction
            if(sign(turtle.vX) != sign(aX)) {
                turtle.vX += 5 * (aX / fps)
            }
            if(sign(turtle.vY) != sign(aY)) {
                turtle.vY += 5 * (aY / fps)
            }
        }

        private fun hasValidPosition(turtle: Turtle, board: Board): Boolean {
            if(outOfBorders(turtle, board)) {
                return false
            }
            else {
                board.turtles.forEach { turtle2 ->
                    if(turtle != turtle2 && doesCollide(turtle, turtle2)) {
                        return false
                    }
                }
            }
            return true
        }

        private fun outOfBorders(turtle: Turtle, board: Board): Boolean {
            return  turtle.x - turtle.r < 0 ||
                    turtle.x + turtle.r > board.width ||
                    turtle.y - turtle.r < 0 ||
                    turtle.y + turtle.r > board.height
        }

        private fun bounceOffBorders(turtle: Turtle, board: Board) {
            if(turtle.x - turtle.r < 0) {
                turtle.x += 2 * abs(turtle.r - turtle.x)
                turtle.vX = -turtle.vX * 0.5f
            }
            if(turtle.x + turtle.r > board.width) {
                turtle.x -= 2 * abs(-board.width + turtle.x + turtle.r)
                turtle.vX = -turtle.vX * 0.5f
            }
            if(turtle.y - turtle.r < 0) {
                turtle.y += 2 * abs(turtle.r - turtle.y)
                turtle.vY = -turtle.vY * 0.5f
            }
            if(turtle.y + turtle.r > board.height) {
                turtle.y -= 2 * abs(-board.height + turtle.y + turtle.r)
                turtle.vY = -turtle.vY  * 0.5f
            }
        }

        private fun collide(a: Turtle, b: Turtle) {
            if(doesCollide(a, b)) {
                val vA = getCollisionVector(a, b)
                val vB = getCollisionVector(b, a)
                a.vX = vA.a
                a.vY = vA.b
                b.vX = vB.a
                b.vY = vB.b
                separate(a, b)
            }
        }

        private fun doesCollide(a: Turtle, b: Turtle): Boolean {
            return distance(a, b) < a.r + b.r
        }

        private fun distance(a: Turtle, b: Turtle): Float {
            return sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y))
        }

        private fun getCollisionVector(a: Turtle, b: Turtle): Vector {
            val xA = Vector(a.x, a.y)
            val xB = Vector(b.x, b.y)
            val vA = Vector(a.vX, a.vY)
            val vB = Vector(b.vX, b.vY)
            val n = (1/(xA-xB).norm()) * (xA - xB)
            var result = (n * (vA - vB)) * n
            result = (2*b.mass)/(a.mass + b.mass) * result
            result = vA - result
            return result
        }

        private fun separate(a: Turtle, b: Turtle) {
            var aPos = Vector(a.x, a.y)
            var bPos = Vector(b.x, b.y)
            val direction = bPos - aPos
            while (doesCollide(a, b)) {
                aPos -= (1/a.mass) * 0.01f * direction
                bPos += (1/b.mass) * 0.01f * direction
                a.x = aPos.a
                a.y = aPos.b
                b.x = bPos.a
                b.y = bPos.b
            }
        }
    }

    class Vector(var a: Float, var b: Float) {
        operator fun plus(second: Vector): Vector {
            return Vector(this.a + second.a, this.b + second.b)
        }

        operator fun minus(second: Vector): Vector {
            return -second + this
        }

        operator fun unaryMinus() = Vector(-a, -b)

        fun norm(): Float {
            return sqrt(a*a + b*b)
        }

        operator fun Float.times(x: Vector) = Vector(this*x.a, this*x.b)

        operator fun times(second: Vector): Float {
            return a*second.a + b*second.b
        }
    }
}

private operator fun Float.times(x: Physics.Vector) = Physics.Vector(this * x.a, this * x.b)
