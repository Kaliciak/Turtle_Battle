package com.kaliciak.turtlebattle.model.turtle

import kotlinx.serialization.Serializable

@Serializable
class TurtleForcesData( val forceX: Float,
                        val forceY: Float,
                        val color: TurtleColor
)