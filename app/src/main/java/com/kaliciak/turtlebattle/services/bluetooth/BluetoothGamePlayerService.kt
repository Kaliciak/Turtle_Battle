package com.kaliciak.turtlebattle.services.bluetooth

import android.util.Log
import com.kaliciak.turtlebattle.viewModel.GamePlayerViewModelDelegate

class BluetoothGamePlayerService(private val delegate: GamePlayerViewModelDelegate) {

    private val socket = BluetoothSavedState.socket
    private val inputStream = socket?.inputStream
    private val outputStream = socket?.outputStream
    private val listenThread = ListenThread()

    init {
        listenThread.start()
    }

    inner class ListenThread: Thread() {
        override fun run() {
            super.run()
            val x = inputStream?.read()
            Log.d("received", "$x")
            if(x == 89) {
                delegate.gameStarted()
            }
        }
    }
}