package com.kaliciak.turtlebattle.view.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.model.Turtle
import com.kaliciak.turtlebattle.model.TurtleColor
import com.kaliciak.turtlebattle.model.TurtleState
import com.kaliciak.turtlebattle.viewModel.GameViewModel

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), GameViewDelegate {
    var viewModel: GameViewModel? = null

    var paint: Paint = Paint(0).apply {
        color = ContextCompat.getColor(context, R.color.black)
    }
    var hToWRatio: Float = 20f

    var paints: MutableMap<TurtleColor, Paint> = mutableMapOf()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GameView,
            0, 0).apply {
            try {
                hToWRatio = getFloat(R.styleable.GameView_h_to_w_ratio, 10f)
            } finally {
                recycle()
            }
        }
        assignPaints()
    }

    private fun drawTurtle(turtle: TurtleState, canvas: Canvas) {
        val x = turtle.x / viewModel!!.boardWidth
        val y = turtle.y / viewModel!!.boardHeight
        val rx = turtle.r / viewModel!!.boardWidth
        val ry = turtle.r / viewModel!!.boardHeight

        canvas.drawOval((x-rx)*width - 3, (y-ry)*height - 3, (x+rx)*width + 3, (y+ry)*height + 3,
            paint
        )
        canvas.drawOval((x-rx)*width, (y-ry)*height, (x+rx)*width, (y+ry)*height,
            paints[turtle.color]!!
        )
    }

    private fun drawBoard(canvas: Canvas) {
        viewModel?.getBoardState()?.turtles?.forEach {
            drawTurtle(it, canvas)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            background.draw(canvas)
            drawBoard(this)
        }
    }

    override fun notifyOnStateChanged() {
        invalidate()
    }

    private fun assignPaints() {
        paints[TurtleColor.BLACK] = Paint(0).apply { color = ContextCompat.getColor(context, R.color.black) }
        paints[TurtleColor.WHITE] = Paint(0).apply { color = ContextCompat.getColor(context, R.color.white) }
        paints[TurtleColor.RED] = Paint(0).apply { color = ContextCompat.getColor(context, R.color.red) }
        paints[TurtleColor.PURPLE] = Paint(0).apply { color = ContextCompat.getColor(context, R.color.purple) }
        paints[TurtleColor.BLUE] = Paint(0).apply { color = ContextCompat.getColor(context, R.color.blue) }
    }
}