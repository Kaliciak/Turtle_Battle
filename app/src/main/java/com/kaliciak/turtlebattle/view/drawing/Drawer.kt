package com.kaliciak.turtlebattle.view.drawing

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.kaliciak.turtlebattle.model.board.coastline.Point

class Drawer {
    companion object {
        fun drawPoly(canvas: Canvas, paint: Paint, points: List<Point>) {
            // line at minimum
            if (points.size < 2) {
                return
            }

            // path
            val polyPath = Path()
            polyPath.moveTo(points[0].x, points[0].y)
            points.forEach { point ->
                polyPath.lineTo(point.x, point.y)
            }
            polyPath.lineTo(points[0].x, points[0].y)

            // draw
            canvas.drawPath(polyPath, paint)
        }
    }
}