package com.kaliciak.turtlebattle.view.foundGames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaliciak.turtlebattle.databinding.ActivityFoundGamesBinding
import com.kaliciak.turtlebattle.view.game.GameActivity
import com.kaliciak.turtlebattle.view.adapters.FoundGamesRecViewAdapter
import com.kaliciak.turtlebattle.viewModel.foundGames.FoundGamesViewModel

class FoundGamesActivity: AppCompatActivity(), FoundGamesActivityDelegate,
    GameJoinActivityDelegate {
    private lateinit var binding: ActivityFoundGamesBinding
    private var viewModel: FoundGamesViewModel? = null
    private val adapter = FoundGamesRecViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoundGamesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = FoundGamesViewModel(this, this)

        binding.fgPlayButton.setOnClickListener {
            playGame(true)
        }
        binding.calibrateButton.setOnClickListener(this::calibrate)
        binding.defaultCalibrationButton.setOnClickListener(this::defaultCalibration)
        binding.foundGamesList.adapter = adapter
        binding.foundGamesList.layoutManager = LinearLayoutManager(this)

        binding.refreshButton.setOnClickListener {
            viewModel?.scan()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onDestroy()
    }

    private fun playGame(isHost: Boolean) {
        val intent = Intent(this, GameActivity::class.java)
        val calibrationData = viewModel?.calibrationData
        intent.putExtra("xCalibration", calibrationData?.x ?: 0f)
        intent.putExtra("yCalibration", calibrationData?.y ?: 0f)
        intent.putExtra("zCalibration", calibrationData?.z ?: 0f)
        viewModel?.startGame()
        intent.putExtra("isHost", isHost)
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

    override fun foundDevice() {
        if(viewModel != null) {
            adapter.updateData(viewModel!!.getDeviceList())
        }
    }

    override fun joinedGame() {
        playGame(false)
    }

    override fun joinDevice(mac: String) {
        viewModel?.joinGame(mac)
    }
}