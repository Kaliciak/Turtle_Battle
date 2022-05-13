package com.kaliciak.turtlebattle.viewModel

import android.content.Context
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import com.kaliciak.turtlebattle.model.CalibrationData
import com.kaliciak.turtlebattle.services.RotationVectorListenerDelegate
import com.kaliciak.turtlebattle.services.SingleRotationVectorListener

class FoundGamesViewModel(private val context: Context): RotationVectorListenerDelegate {
    var calibrationData = CalibrationData()
        private set

    private var rotationListener: SingleRotationVectorListener = SingleRotationVectorListener(this,
        context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    )

    fun calibrate() {
        rotationListener.getData()
    }

    fun defaultCalibration() {
        calibrationData.x = 0f
        calibrationData.y = 0f
        calibrationData.z = 0f
    }

    override fun didChange(valX: Float, valY: Float, valZ: Float) {
        calibrationData.x = valX
        calibrationData.y = valY
        calibrationData.z = valZ
//        Log.d("calibrate", "${calibrationData.x}, ${calibrationData.y}, ${calibrationData.z}")
    }
}