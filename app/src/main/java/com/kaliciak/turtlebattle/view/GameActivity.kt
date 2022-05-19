package com.kaliciak.turtlebattle.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.kaliciak.turtlebattle.Utility
import com.kaliciak.turtlebattle.databinding.ActivityGameBinding
import com.kaliciak.turtlebattle.model.TurtleColor
import com.kaliciak.turtlebattle.viewModel.GameHostViewModel
import com.kaliciak.turtlebattle.viewModel.GamePlayerViewModel
import com.kaliciak.turtlebattle.viewModel.GameViewModel
import kotlin.math.round


class GameActivity: AppCompatActivity(), GameActivityDelegate {
    private lateinit var binding: ActivityGameBinding
    private var viewModel: GameViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val isHost = intent.getBooleanExtra("isHost", false)

        if(isHost) {
            if(viewModel == null) {
                viewModel = GameHostViewModel(this, this)
            }
        }
        else {
            if(viewModel == null) {
                viewModel = GamePlayerViewModel(this, this)
            }

            startGame()
//            viewModel?.startGame()
        }

        binding.startGameButton.setOnClickListener {
            viewModel?.startGame()
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

    override fun notifyOnStateChanged() {
        binding.gameView.notifyOnStateChanged()
    }

    override fun playerConnected() {
        runOnUiThread {
            binding.waitingText.visibility = View.INVISIBLE
            binding.readyText.visibility = View.VISIBLE
            binding.startGameButton.visibility = View.VISIBLE
        }
        notifyOnStateChanged()
    }

    override fun gameOver(name: String, turtleColor: TurtleColor) {
        binding.turtleNameText.text = name
        binding.turtleNameText.visibility = View.VISIBLE
        binding.turtleNameText.setTextColor(binding.gameView.paints[turtleColor]!!.color)
        binding.wonText.visibility = View.VISIBLE
    }

    override fun startGame() {
        binding.startGameButton.visibility = View.INVISIBLE
        binding.waitingText.visibility = View.INVISIBLE
        binding.readyText.visibility = View.INVISIBLE
    }
}