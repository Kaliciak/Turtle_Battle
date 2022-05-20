package com.kaliciak.turtlebattle.model.turtle

import kotlinx.serialization.Serializable

@Serializable
class TurtleData(val x: Float,
                 val y: Float,
                 val forceX: Float,
                 val forceY: Float,
                 val vX: Float,
                 val vY: Float,
                 val color: TurtleColor
)