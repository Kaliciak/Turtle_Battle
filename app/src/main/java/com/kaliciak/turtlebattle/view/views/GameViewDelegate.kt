package com.kaliciak.turtlebattle.view.views

import com.kaliciak.turtlebattle.model.TurtleColor

interface GameViewDelegate {
    fun notifyOnStateChanged()
    fun gameOver(name: String, turtleColor: TurtleColor)
}