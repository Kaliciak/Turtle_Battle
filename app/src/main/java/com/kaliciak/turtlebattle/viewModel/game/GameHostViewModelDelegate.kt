package com.kaliciak.turtlebattle.viewModel.game

interface GameHostViewModelDelegate {
    fun playerConnected()
    fun gotMessage(message: String)
}