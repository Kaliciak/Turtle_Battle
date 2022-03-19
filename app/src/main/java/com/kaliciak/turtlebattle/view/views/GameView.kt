package com.kaliciak.turtlebattle.view.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.kaliciak.turtlebattle.R

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var paint: Paint = Paint(0).apply {
        color = ContextCompat.getColor(context, R.color.black)
    }
    var hToWRatio: Float = 20f

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
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawCircle(200f, 200f, 100f, paint)
        }
    }
}