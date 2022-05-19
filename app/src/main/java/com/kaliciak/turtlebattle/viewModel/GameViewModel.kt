package com.kaliciak.turtlebattle.viewModel

import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.model.BoardState

interface GameViewModel: GameViewModelDelegate {
    val boardWidth: Int
    val boardHeight: Int

    fun stop()
    fun calibrate(x: Float, y: Float, z: Float)
    fun startGame()
    fun getBoardState(): BoardState
}