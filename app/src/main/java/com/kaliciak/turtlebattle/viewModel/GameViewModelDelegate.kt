package com.kaliciak.turtlebattle.viewModel

import com.kaliciak.turtlebattle.model.Turtle

interface GameViewModelDelegate {
    fun gameOver(turtle: Turtle?)
    fun playerConnected()
}