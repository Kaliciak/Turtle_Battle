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

class MainActivity : AppCompatActivity(), RotationVectorListenerDelegate {
    private lateinit var binding: ActivityMainBinding
    private var calibrationData = CalibrationData()
    private lateinit var rotationListener: SingleRotationVectorListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.playButton.setOnClickListener(this::playGame)
        binding.exitButton.setOnClickListener(this::exitApp)
        binding.calibrateButton.setOnClickListener(this::calibrate)
        binding.defaultCalibrationButton.setOnClickListener(this::defaultCalibration)
        rotationListener = SingleRotationVectorListener(this,
            getSystemService(SENSOR_SERVICE) as SensorManager)
    }

    fun playGame(v: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("xCalibration", calibrationData.x)
        intent.putExtra("yCalibration", calibrationData.y)
        intent.putExtra("zCalibration", calibrationData.z)
        startActivity(intent)
    }

    fun exitApp(v: View) {
        this@MainActivity.finish()
        exitProcess(0)
    }

    fun calibrate(v: View) {
        rotationListener.getData()
    }

    fun defaultCalibration(v: View) {
        calibrationData.x = 0f
        calibrationData.y = 0f
        calibrationData.z = 0f
        Toast.makeText(this, "Default calibration data", Toast.LENGTH_SHORT).show()
    }

    override fun didChange(valX: Float, valY: Float, valZ: Float) {
        calibrationData.x = valX
        calibrationData.y = valY
        calibrationData.z = valZ
//        Log.d("calibrate", "${calibrationData.x}, ${calibrationData.y}, ${calibrationData.z}")
        Toast.makeText(this, "Calibrated successfully", Toast.LENGTH_SHORT).show()
    }
}