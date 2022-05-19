package com.kaliciak.turtlebattle.model

import android.util.Log
import com.kaliciak.turtlebattle.viewModel.GameViewModelDelegate
import java.lang.Exception

class Board(val width: Int,
            val height: Int,
            turtles: List<Turtle>,
            val fps: Int,
            val delegate: GameViewModelDelegate) {

    var turtles: List<Turtle> = turtles
        @Synchronized set
        @Synchronized get

    private val coastlineManager = CoastlineManager(width, height, fps)

    fun getState(): BoardState {
        return BoardState(height, width, turtles.map { turtle ->  turtle.getState()}, coastlineManager.coastline)
    }

    fun getCoastline(): Coastline {
        return coastlineManager.coastline
    }

    private fun getCoastX(lowerPoint: Point, upperPoint: Point, y: Float): Float {
        return lowerPoint.x - (lowerPoint.x - upperPoint.x) * (lowerPoint.y - y) / (lowerPoint.y - upperPoint.y)
    }

    private fun isOutsideCoastline(turtle: Turtle): Boolean {
        if(turtle.x < 0 || turtle.x > width) {
            return true
        }

        val coastline = coastlineManager.coastline
        var prevY = coastline.left[0].y
        var nextY = coastline.left[1].y
        var index = 1
        while(turtle.y !in nextY..prevY) {
            prevY = nextY
            index += 1
            nextY = coastline.left[index].y
        }

        if(turtle.x < getCoastX(coastline.left[index-1], coastline.left[index], turtle.y) ||
            turtle.x > getCoastX(coastline.right[index-1], coastline.right[index], turtle.y)) {
            return true
        }

        return false
    }

    private fun checkCoastline() {
        val newTurtles = turtles.toMutableList()
        turtles.forEach { turtle ->
            if(isOutsideCoastline(turtle)) {
                newTurtles.remove(turtle)
            }
        }
        turtles = newTurtles
    }

    fun tick() {
        coastlineManager.step()
        Physics.step(this)
        checkCoastline()

        if(turtles.size <= 1) {
            delegate.gameOver(turtles.first())
        }
    }

    @Synchronized
    fun getBoardData(): BoardData {
        val turtlesDataList = mutableListOf<TurtleData>()
        for(turtle in turtles) {
            val turtleData = turtle.getTurtleData()
            turtlesDataList.add(turtleData)
        }

        return BoardData(turtlesDataList, coastlineManager.coastline, coastlineManager.speed)
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
    }
}