package com.kaliciak.turtlebattle.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaliciak.turtlebattle.databinding.ActivityFoundGamesBinding
import com.kaliciak.turtlebattle.view.adapters.FoundGamesRecViewAdapter
import com.kaliciak.turtlebattle.viewModel.FoundGamesViewModel

class FoundGamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoundGamesBinding
    private var viewModel: FoundGamesViewModel? = null
    private val adapter = FoundGamesRecViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoundGamesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = FoundGamesViewModel(this)

        binding.fgPlayButton.setOnClickListener(this::playGame)
        binding.calibrateButton.setOnClickListener(this::calibrate)
        binding.defaultCalibrationButton.setOnClickListener(this::defaultCalibration)
        binding.foundGamesList.adapter = adapter
        binding.foundGamesList.layoutManager = LinearLayoutManager(this)
    }

    private fun playGame(v: View) {
        val intent = Intent(this, GameActivity::class.java)
        val calibrationData = viewModel?.calibrationData
        intent.putExtra("xCalibration", calibrationData?.x ?: 0f)
        intent.putExtra("yCalibration", calibrationData?.y ?: 0f)
        intent.putExtra("zCalibration", calibrationData?.z ?: 0f)
        startActivity(intent)
    }

    private fun calibrate(v: View) {
        viewModel?.calibrate()
        Toast.makeText(this, "Calibrated successfully", Toast.LENGTH_SHORT).show()
    }

    private fun defaultCalibration(v: View) {
        viewModel?.defaultCalibration()
        Toast.makeText(this, "Default calibration data", Toast.LENGTH_SHORT).show()
    }
}