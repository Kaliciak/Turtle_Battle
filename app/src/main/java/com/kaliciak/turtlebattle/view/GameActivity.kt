package com.kaliciak.turtlebattle.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.kaliciak.turtlebattle.Utility
import com.kaliciak.turtlebattle.databinding.ActivityGameBinding
import com.kaliciak.turtlebattle.viewModel.GameViewModel
import kotlin.math.round


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var viewModel: GameViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(viewModel == null) {
            viewModel = GameViewModel(resources, this, binding.gameView)
        }
        binding.gameView.viewModel = viewModel
        binding.gameView.binding = binding
        setGameViewSize()
        calibrate()
    }

    override fun onStop() {
        viewModel?.stop()
        super.onStop()
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

    private fun calibrate() {
        val x = intent.getFloatExtra("xCalibration", 0f)
        val y = intent.getFloatExtra("yCalibration", 0f)
        val z = intent.getFloatExtra("zCalibration", 0f)
        viewModel?.calibrate(x, y, z)
    }
}