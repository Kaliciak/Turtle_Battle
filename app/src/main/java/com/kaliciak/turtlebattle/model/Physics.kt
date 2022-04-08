package com.kaliciak.turtlebattle.model

import android.util.Log
import kotlin.math.sqrt

class Physics {
    companion object {
        fun collide(a: Turtle, b: Turtle) {
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
            while (doesCollide(a, b)) {
                a.x += a.vX * 0.01f
                a.y += a.vY * 0.01f
                b.x += b.vX * 0.01f
                b.y += b.vY * 0.01f
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
