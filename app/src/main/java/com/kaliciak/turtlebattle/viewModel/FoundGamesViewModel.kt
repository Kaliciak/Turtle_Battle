package com.kaliciak.turtlebattle.viewModel

import android.annotation.SuppressLint
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.kaliciak.turtlebattle.model.CalibrationData
import com.kaliciak.turtlebattle.services.bluetooth.BluetoothSearchGameService
import com.kaliciak.turtlebattle.services.sensors.RotationVectorListenerDelegate
import com.kaliciak.turtlebattle.services.sensors.SingleRotationVectorListener

class FoundGamesViewModel(private val activity: FragmentActivity): RotationVectorListenerDelegate {
    var calibrationData = CalibrationData()
        private set

    private var rotationListener: SingleRotationVectorListener = SingleRotationVectorListener(this,
        activity.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    )

    @SuppressLint("NewApi")
    private val searchGameService: BluetoothSearchGameService = BluetoothSearchGameService(activity)

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