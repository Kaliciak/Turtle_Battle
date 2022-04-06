package com.kaliciak.turtlebattle.viewModel

import android.content.Context
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.model.*
import com.kaliciak.turtlebattle.services.RotationVectorListener
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
//    private val rotationVectorListener = RotationVectorListener(this,
//        context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager)
    private lateinit var player: Player

    init {
        val turtle = Turtle(20f, 20f, 10f, 1, TurtleColor.BLUE)
        val turtle2 = Turtle(50f, 20f, 10f, 2, TurtleColor.RED)
        val turtle3 = Turtle(100f, 70f, 20f, 3, TurtleColor.WHITE)
        val turtle4 = Turtle((boardWidth/2).toFloat(), (boardHeight/2).toFloat(), 12f, 1, TurtleColor.PURPLE)
        player = Player(turtle4, context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager)
        val turtles = listOf(turtle, turtle2, turtle3, turtle4)
        board = Board(boardWidth, boardHeight, turtles, fps)
        handler.postDelayed(tickClock, ((1000)/fps).toLong())
    }

    fun getBoardState(): BoardState {
        return board.getState()
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
