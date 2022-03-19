package com.kaliciak.turtlebattle.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kaliciak.turtlebattle.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.playButton.setOnClickListener(this::playGame)
        binding.exitButton.setOnClickListener(this::exitApp)
    }

    fun playGame(v: View) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    fun exitApp(v: View) {
        this@MainActivity.finish()
        exitProcess(0)
    }
}