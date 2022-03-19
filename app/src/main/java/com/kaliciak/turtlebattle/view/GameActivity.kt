package com.kaliciak.turtlebattle.view

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.kaliciak.turtlebattle.Utility
import com.kaliciak.turtlebattle.databinding.ActivityGameBinding
import kotlin.math.round


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setGameViewSize()
    }

    private fun setGameViewSize() {
        val ratio = binding.gameView.hToWRatio
        val rootWidth = Utility.getSystemWidth()
        val rootHeight = Utility.getSystemHeight()
        var viewWidth = 0
        var viewHeight = 0

        if(rootHeight / ratio <= rootWidth) {
            viewHeight = rootHeight
            viewWidth = round(rootHeight / ratio).toInt()
        }
        else {
            viewWidth = rootWidth
            viewHeight = round(rootWidth * ratio).toInt()
        }

        binding.gameView.updateLayoutParams {
            width = viewWidth
            height = viewHeight
        }
    }
}