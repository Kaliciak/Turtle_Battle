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
                    private val delegate: GameViewDelegate): GameViewModelDelegate {
    val boardWidth = resources.getInteger(R.integer.gameWidth)
    val boardHeight = resources.getInteger(R.integer.gameHeight)
    private val fps = 30
    private val board: Board
    private var handler = Handler(Looper.getMainLooper())
    private val tickClock: TickClock = TickClock()
    private var player: Player
    private var calibrationData = CalibrationData()
    private var stopped = false

    init {
        val turtle1 = Turtle((boardWidth/2).toFloat(), (boardHeight/2).toFloat(), 12f, 2f, TurtleColor.PURPLE)
        val turtle2 = Turtle((boardWidth/2).toFloat(), 50f, 12f, 2f, TurtleColor.RED)
        player = Player(turtle1, context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager, calibrationData)
        val turtles = listOf(turtle1, turtle2)
        board = Board(boardWidth, boardHeight, turtles, fps, this)
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
