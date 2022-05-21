package com.kaliciak.turtlebattle.viewModel.game

import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.model.*
import com.kaliciak.turtlebattle.model.board.Board
import com.kaliciak.turtlebattle.model.board.BoardData
import com.kaliciak.turtlebattle.model.board.BoardState
import com.kaliciak.turtlebattle.model.turtle.Turtle
import com.kaliciak.turtlebattle.model.turtle.TurtleColor
import com.kaliciak.turtlebattle.model.turtle.TurtleForcesData
import com.kaliciak.turtlebattle.services.bluetooth.BluetoothGamePlayerService
import com.kaliciak.turtlebattle.view.game.GameActivityDelegate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GamePlayerViewModel(private val activity: FragmentActivity,
                          private val delegate: GameActivityDelegate
):
    GamePlayerViewModelDelegate,
    GameViewModel {

    override val boardWidth = activity.resources.getInteger(R.integer.gameWidth)
    override val boardHeight = activity.resources.getInteger(R.integer.gameHeight)
    private val fps = 30
    // messages per second
    private val mps = 10
    private val board: Board
    private var handler = Handler(Looper.getMainLooper())
    private val tickClock = TickClock()
    private val sendDataClock = SendDataClock()
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
            handler.postDelayed(sendDataClock, ((1000)/mps).toLong())
        }
    }

    fun stopGame() {
        handler.removeCallbacksAndMessages(null)
        stopped = true
        player.stop()
    }

    override fun stop() {
        stopGame()
        bluetoothService.stop()
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
        stopGame()
    }


    override fun gameStarted() {
        startGame()
    }

    override fun gotMessage(message: String) {
        try {
            val boardData = Json.decodeFromString<BoardData>(message)
            board.applyBoardData(boardData)
        } catch (e: Exception) {
            Log.d("EXCEPTION", "$e")
        }
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

    inner class SendDataClock: Runnable {
        override fun run() {
            val forces = player.turtle.getForcesData()
            val message = Json.encodeToString(forces)
            bluetoothService.sendData(message)
            if(!stopped) {
                handler.postDelayed(this, ((1000)/mps).toLong())
            }
        }
    }
}