package com.kaliciak.turtlebattle.model.board

import com.kaliciak.turtlebattle.model.board.coastline.Coastline
import com.kaliciak.turtlebattle.model.turtle.TurtleState

class BoardState(val height: Int,
                 val width: Int,
                 val turtles: List<TurtleState>,
                 val coastline: Coastline
)
