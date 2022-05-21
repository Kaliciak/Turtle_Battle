package com.kaliciak.turtlebattle.services.bluetooth

import android.util.Log
import com.kaliciak.turtlebattle.viewModel.game.GamePlayerViewModelDelegate

class BluetoothGamePlayerService(private val delegate: GamePlayerViewModelDelegate) {

    private val charset = Charsets.UTF_8

    private val socket = BluetoothSavedState.socket
    private val inputStream = socket?.inputStream
    private val outputStream = socket?.outputStream
    private val startListenThread = StartListenThread()
    private val listenThread = ListenThread()
    private var sendThread: SendThread? = null

    init {
        startListenThread.start()
    }

    fun sendData(message: String) {
        sendThread = SendThread(message)
        sendThread?.start()
    }

    fun stop() {
        socket?.close()
        listenThread.stopThread()
    }

    inner class StartListenThread: Thread() {
        override fun run() {
            super.run()
            try {
                val x = inputStream?.read()
                Log.d("received", "$x")
                if (x == 89) {
                    delegate.gameStarted()
                    listenThread.start()
                }
            } catch (e: Exception) {
                Log.d("EXCEPTION", "$e")
            }
        }
    }

    inner class ListenThread: Thread() {
        private var stopped = false
            @Synchronized set
            @Synchronized get

        override fun run() {
            super.run()
            try {
                while(!stopped) {
                    val received = ByteArray(2048)
                    val bytes = inputStream?.read(received)
                    if (bytes == null || bytes <= 0) {
                        Log.d("received", "$bytes")
                        Log.d("inpstr", "$inputStream")
                    }
                    else {
                        val str = String(received.copyOfRange(0, bytes), charset)
                        Log.d("received", str)
                        delegate.gotMessage(str)
                    }
                }
            } catch (e: Exception) {
                Log.d("EXCEPTION", "$e")
            }
        }

        fun stopThread() {
            stopped = true
        }
    }

    inner class SendThread(private val message: String): Thread() {
        override fun run() {
            super.run()
            val bytes = message.toByteArray(charset)
            try {
                outputStream?.write(bytes)
            } catch (e: Exception) {
                Log.d("EXCEPTION", "$e")
            }
//            Log.d("written", message)
        }
    }
}