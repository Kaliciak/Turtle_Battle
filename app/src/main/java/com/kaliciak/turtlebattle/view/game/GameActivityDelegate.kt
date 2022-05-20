package com.kaliciak.turtlebattle.view.game

import com.kaliciak.turtlebattle.model.turtle.TurtleColor

interface GameActivityDelegate {
    fun notifyOnStateChanged()
    fun playerConnected()
    fun gameOver(name: String, turtleColor: TurtleColor)
    fun startGame()
}