package com.kaliciak.turtlebattle.viewModel

import android.content.Context
import android.content.res.Resources
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.model.*
import com.kaliciak.turtlebattle.view.views.GameViewDelegate

class GameViewModel(private val resources: Resources,
                    private val context: Context,
                    private val delegate: GameViewDelegate) {
    val boardWidth = resources.getInteger(R.integer.gameWidth)
    val boardHeight = resources.getInteger(R.integer.gameHeight)
    private val fps = 30
    private val board: Board
    private var handler = Handler(Looper.getMainLooper())
    private val tickClock: TickClock = TickClock()
    private var player: Player
    private var calibrationData = CalibrationData()

    init {
        val turtle = Turtle(20f, 20f, 10f, 1f, TurtleColor.BLUE)
        val turtle2 = Turtle(50f, 20f, 10f, 2f, TurtleColor.RED)
        val turtle3 = Turtle(100f, 70f, 20f, 3f, TurtleColor.WHITE)
        val turtle4 = Turtle((boardWidth/2).toFloat(), (boardHeight/2).toFloat(), 12f, 2f, TurtleColor.PURPLE)
        player = Player(turtle4, context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager, calibrationData)
        val turtles = listOf(turtle, turtle2, turtle3, turtle4)
        board = Board(boardWidth, boardHeight, turtles, fps)
        handler.postDelayed(tickClock, ((1000)/fps).toLong())
    }

    fun calibrate(x: Float, y: Float, z: Float) {
        calibrationData.x = x
        calibrationData.y = y
        calibrationData.z = z
    }

    fun getBoardState(): BoardState {
        return board.getState()
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        player.stop()
    }

    fun getPlayerTurtle(): Turtle {
        return player.turtle
    }

    inner class TickClock: Runnable {
        override fun run() {
            board.tick()
            player.tick()
            delegate.notifyOnStateChanged()
            handler.postDelayed(this, ((1000)/fps).toLong())
        }
    }
}
