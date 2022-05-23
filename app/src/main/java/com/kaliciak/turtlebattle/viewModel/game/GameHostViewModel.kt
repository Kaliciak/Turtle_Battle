package com.kaliciak.turtlebattle.viewModel.game

import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.model.CalibrationData
import com.kaliciak.turtlebattle.model.Player
import com.kaliciak.turtlebattle.model.board.Board
import com.kaliciak.turtlebattle.model.board.BoardState
import com.kaliciak.turtlebattle.model.turtle.Turtle
import com.kaliciak.turtlebattle.model.turtle.TurtleColor
import com.kaliciak.turtlebattle.model.turtle.TurtleForcesData
import com.kaliciak.turtlebattle.services.bluetooth.BluetoothGameHostService
import com.kaliciak.turtlebattle.view.game.GameActivityDelegate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameHostViewModel(private val activity: FragmentActivity,
                        private val delegate: GameActivityDelegate
):
    GameHostViewModelDelegate,
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

    private val hostService = BluetoothGameHostService(activity, this)

    init {
        val turtle1 = Turtle((boardWidth/2).toFloat() - 50f, (boardHeight/2).toFloat(), 15f, 2f, TurtleColor.PURPLE)
        player = Player(turtle1, activity.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager, calibrationData)
        val turtles = listOf(turtle1)
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
        hostService.sendStartMessage()
        delegate.startGame()
        handler.postDelayed(tickClock, ((1000)/fps).toLong())
    }

    private fun stopGame() {
        handler.removeCallbacksAndMessages(null)
        stopped = true
        player.stop()
    }

    override fun stop() {
        stopGame()
        hostService.stop()
    }

    override fun gameOver() {
        val aliveTurtles = board.getAliveTurtles()
        if(aliveTurtles.isEmpty()) {
            delegate.gameOver("NONE", TurtleColor.WHITE)
        }
        else {
            val turtle = aliveTurtles.first()
            val color = turtle.color
            delegate.gameOver(color.name, color)
        }

        sendBoardData()
        stopGame()
    }

    override fun playerConnected() {
        val turtle2 = Turtle((boardWidth/2).toFloat() + 50f, (boardHeight/2).toFloat(), 15f, 2f, TurtleColor.RED)
        val mTurtles = board.turtles.toMutableList()
        mTurtles.add(turtle2)
        board.turtles = mTurtles
        delegate.playerConnected()
    }

    private fun sendBoardData() {
        val boardData = board.getBoardData()
        val message = Json.encodeToString(boardData)
        hostService.sendBoardData(message)
    }

    override fun gotMessage(message: String) {
        try {
            val forcesData = Json.decodeFromString<TurtleForcesData>(message)
            board.applyForcesData(forcesData)
            sendBoardData()
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
}
