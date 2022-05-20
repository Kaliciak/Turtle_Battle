package com.kaliciak.turtlebattle.view.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.databinding.ActivityGameBinding
import com.kaliciak.turtlebattle.model.board.BoardState
import com.kaliciak.turtlebattle.model.board.coastline.Point
import com.kaliciak.turtlebattle.model.turtle.TurtleColor
import com.kaliciak.turtlebattle.model.turtle.TurtleState
import com.kaliciak.turtlebattle.view.drawing.Drawer
import com.kaliciak.turtlebattle.viewModel.game.GameViewModel

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var viewModel: GameViewModel? = null

    var paint: Paint = Paint(0).apply {
        color = ContextCompat.getColor(context, R.color.black)
    }
    var hToWRatio: Float = 20f
    var paints: MutableMap<TurtleColor, Paint> = mutableMapOf()
    var binding: ActivityGameBinding? = null

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
        if(viewModel == null) {
            return
        }
        val board = viewModel!!.getBoardState()
        drawBorders(canvas, board)
        board.turtles.forEach {
            drawTurtle(it, canvas)
        }
    }

    private fun drawBorders(canvas: Canvas, board: BoardState) {
        drawLeftBorder(canvas, board)
        drawRightBorder(canvas, board)
    }

    private fun drawLeftBorder(canvas: Canvas, board: BoardState) {
        val shape = mutableListOf(Point(0f, canvas.height.toFloat()))
        shape += board.coastline.left.map { point -> mapPoint(point, canvas, board) }
        shape += listOf(Point(0f, 0f))
        Drawer.drawPoly(canvas, paints[TurtleColor.BLUE]!!, shape)
    }

    private fun drawRightBorder(canvas: Canvas, board: BoardState) {
        val shape = mutableListOf(Point(canvas.width.toFloat(), canvas.height.toFloat()))
        shape += board.coastline.right.map { point -> mapPoint(point, canvas, board) }
        shape += listOf(Point(canvas.width.toFloat(), 0f))
        Drawer.drawPoly(canvas, paints[TurtleColor.BLUE]!!, shape)
    }

    private fun mapPoint(point: Point, canvas: Canvas, board: BoardState): Point {
        val widthFactor = canvas.width.toFloat() / board.width
        val heightFactor = canvas.height.toFloat() / board.height
        return Point(point.x * widthFactor, point.y * heightFactor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            background.draw(canvas)
            drawBoard(this)
        }
    }

    fun notifyOnStateChanged() {
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