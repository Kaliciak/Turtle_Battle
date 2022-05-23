package com.kaliciak.turtlebattle.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kaliciak.turtlebattle.databinding.ActivityMainBinding
import com.kaliciak.turtlebattle.model.CalibrationData
import com.kaliciak.turtlebattle.view.foundGames.FoundGamesActivity
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