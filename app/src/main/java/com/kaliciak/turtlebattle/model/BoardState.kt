package com.kaliciak.turtlebattle.model

class BoardState(val height: Int,
                 val width: Int,
                 val turtles: List<TurtleState>,
                 val leftBorder: List<Point>,
                 val rightBorder: List<Point>)
