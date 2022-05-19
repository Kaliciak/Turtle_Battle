package com.kaliciak.turtlebattle.viewModel

import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.model.*
import com.kaliciak.turtlebattle.services.bluetooth.BluetoothGameHostService
import com.kaliciak.turtlebattle.services.bluetooth.BluetoothGamePlayerService
import com.kaliciak.turtlebattle.view.GameActivityDelegate

class GamePlayerViewModel(private val activity: FragmentActivity,
                          private val delegate: GameActivityDelegate):
    GamePlayerViewModelDelegate,
    GameViewModel {

    override val boardWidth = activity.resources.getInteger(R.integer.gameWidth)
    override val boardHeight = activity.resources.getInteger(R.integer.gameHeight)
    private val fps = 30
    private val board: Board
    private var handler = Handler(Looper.getMainLooper())
    private val tickClock: TickClock = TickClock()
    private var player: Player
    private var calibrationData = CalibrationData()
    private var stopped = false
    private val bluetoothService = BluetoothGamePlayerService(this)

    init {
        val turtle1 = Turtle((boardWidth/2).toFloat() - 50f, (boardHeight/2).toFloat(), 15f, 2f, TurtleColor.PURPLE)
        val turtle2 = Turtle((boardWidth/2).toFloat() + 50f, (boardHeight/2).toFloat(), 15f, 2f, TurtleColor.RED)
        player = Player(turtle2, activity.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager, calibrationData)
        val turtles = listOf(turtle1, turtle2)
        board = Board(boardWidth, boardHeight, turtles, fps, this)
    }

    override fun calibrate(x: Float, y: Float, z: Float) {
        calibrationData.x = x
        calibrationData.y = y
        calibrationData.z = z
    }

    override fun getBoardState(): BoardState {
        return board.getState()
    }

    override fun startGame() {
        activity.runOnUiThread {
            delegate.startGame()
            handler.postDelayed(tickClock, ((1000)/fps).toLong())
        }
    }

    override fun stop() {
        handler.removeCallbacksAndMessages(null)
        stopped = true
        player.stop()
    }

    fun getPlayerTurtle(): Turtle {
        return player.turtle
    }

    override fun gameOver(turtle: Turtle?) {
        if(turtle == null) {
            delegate.gameOver("NONE", TurtleColor.WHITE)
        }
        else {
            val color = turtle.color
            delegate.gameOver(color.name, color)
        }
        stop()
    }


    override fun gameStarted() {
        startGame()
    }

    inner class TickClock: Runnable {
        override fun run() {
            board.tick()
            player.tick()
            delegate.notifyOnStateChanged()
            if(!stopped) {
                handler.postDelayed(this, ((1000)/fps).toLong())
            }
        }
    }
}