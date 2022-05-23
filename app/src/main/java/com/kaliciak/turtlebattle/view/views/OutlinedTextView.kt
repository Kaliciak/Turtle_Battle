package com.kaliciak.turtlebattle.view.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class OutlinedTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    override fun onDraw(canvas: Canvas?) {
        for(i in 0..10) {
            super.onDraw(canvas)
        }
    }
}