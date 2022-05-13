package com.kaliciak.turtlebattle.view

import android.content.Intent
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kaliciak.turtlebattle.databinding.ActivityMainBinding
import com.kaliciak.turtlebattle.model.CalibrationData
import com.kaliciak.turtlebattle.services.RotationVectorListenerDelegate
import com.kaliciak.turtlebattle.services.SingleRotationVectorListener
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var calibrationData = CalibrationData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.playButton.setOnClickListener(this::playGame)
        binding.exitButton.setOnClickListener(this::exitApp)
    }

    private fun playGame(v: View) {
        val intent = Intent(this, FoundGamesActivity::class.java)
        startActivity(intent)
    }

    private fun exitApp(v: View) {
        this@MainActivity.finish()
        exitProcess(0)
    }
}