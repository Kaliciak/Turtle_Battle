package com.kaliciak.turtlebattle.viewModel.game

import com.kaliciak.turtlebattle.model.board.BoardState

interface GameViewModel: GameViewModelDelegate {
    val boardWidth: Int
    val boardHeight: Int

    fun stop()
    fun calibrate(x: Float, y: Float, z: Float)
    fun startGame()
    fun getBoardState(): BoardState
}