package com.kaliciak.turtlebattle.viewModel.game

interface GamePlayerViewModelDelegate {
    fun gameStarted()
    fun gotMessage(message: String)
}