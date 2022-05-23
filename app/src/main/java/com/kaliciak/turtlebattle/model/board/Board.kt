package com.kaliciak.turtlebattle.model.board

import android.util.Log
import com.kaliciak.turtlebattle.model.board.coastline.Coastline
import com.kaliciak.turtlebattle.model.board.coastline.CoastlineManager
import com.kaliciak.turtlebattle.model.turtle.Turtle
import com.kaliciak.turtlebattle.model.turtle.TurtleData
import com.kaliciak.turtlebattle.model.turtle.TurtleForcesData
import com.kaliciak.turtlebattle.viewModel.game.GameViewModelDelegate

class Board(val width: Int,
            val height: Int,
            turtles: List<Turtle>,
            val fps: Int,
            val delegate: GameViewModelDelegate
) {

    var turtles: List<Turtle> = turtles
        @Synchronized set
        @Synchronized get

    var gameOver = false
        @Synchronized set
        @Synchronized get

    private val coastlineManager = CoastlineManager(width, height, fps)

    fun getState(): BoardState {
        return BoardState(height, width, turtles.filter { turtle -> turtle.alive }.map { turtle ->  turtle.getState()}, coastlineManager.coastline)
    }

    fun getCoastline(): Coastline {
        return coastlineManager.coastline
    }

    private fun checkCoastline() {
        turtles.forEach { turtle ->
            if(coastlineManager.isOutsideCoastline(turtle)) {
                turtle.alive = false
            }
        }
    }

    fun getAliveTurtles(): List<Turtle> = turtles.filter { turtle -> turtle.alive }

    fun tick() {
        coastlineManager.step()
        Physics.step(this)
        checkCoastline()

        val aliveTurtles = getAliveTurtles()
        if(aliveTurtles.size <= 1) {
            gameOver = true
            delegate.gameOver()
        }
    }

    @Synchronized
    fun getBoardData(): BoardData {
        val turtlesDataList = mutableListOf<TurtleData>()
        for(turtle in turtles) {
            val turtleData = turtle.getTurtleData()
            turtlesDataList.add(turtleData)
        }

        return BoardData(turtlesDataList, coastlineManager.coastline, coastlineManager.speed, gameOver)
    }

    @Synchronized
    fun applyBoardData(boardData: BoardData) {
        val turtlesData = boardData.turtles
        for(turtleData in turtlesData) {
            try {
                val turtle = turtles.first { it.color == turtleData.color }
                turtle.applyTurtleData(turtleData)
            } catch (e: Exception) {
                Log.d("EXCEPTION", "${turtleData.color} turtle not found")
            }
        }

        gameOver = boardData.gameOver
        coastlineManager.applyCoastline(boardData.coastline, boardData.speed)
    }

    @Synchronized
    fun applyForcesData(forcesData: TurtleForcesData) {
        try {
            val turtle = turtles.first { it.color == forcesData.color }
            turtle.applyForcesData(forcesData)
        } catch (e: Exception) {
            Log.d("EXCEPTION", "${forcesData.color} turtle not found")
        }
    }
}