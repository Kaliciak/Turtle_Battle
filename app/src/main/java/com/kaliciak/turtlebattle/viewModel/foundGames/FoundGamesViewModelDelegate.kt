package com.kaliciak.turtlebattle.viewModel.foundGames

import com.kaliciak.turtlebattle.model.bluetooth.Device

interface FoundGamesViewModelDelegate {
    fun foundDevice(device: Device)
    fun restartScan()
    fun joinedGame()
}