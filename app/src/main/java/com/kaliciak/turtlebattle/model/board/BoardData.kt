package com.kaliciak.turtlebattle.model.board

import com.kaliciak.turtlebattle.model.board.coastline.Coastline
import com.kaliciak.turtlebattle.model.turtle.TurtleData
import kotlinx.serialization.Serializable

@Serializable
class BoardData(val turtles: List<TurtleData>,
                val coastline: Coastline,
                val speed: Float,
                val gameOver: Boolean)