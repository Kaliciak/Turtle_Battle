package com.kaliciak.turtlebattle.view

import com.kaliciak.turtlebattle.model.TurtleColor

interface GameActivityDelegate {
    fun notifyOnStateChanged()
    fun gameOver(name: String, turtleColor: TurtleColor)
    fun startGame()
}