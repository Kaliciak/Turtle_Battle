package com.kaliciak.turtlebattle.viewModel
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.kaliciak.turtlebattle.model.CalibrationData
import com.kaliciak.turtlebattle.model.bluetooth.Device
import com.kaliciak.turtlebattle.services.bluetooth.BluetoothSearchGameService
import com.kaliciak.turtlebattle.services.sensors.RotationVectorListenerDelegate
import com.kaliciak.turtlebattle.services.sensors.SingleRotationVectorListener
import com.kaliciak.turtlebattle.view.FoundGamesActivityDelegate

class FoundGamesViewModel(
    private val activity: FragmentActivity,
    private val delegate: FoundGamesActivityDelegate):
    RotationVectorListenerDelegate, FoundGamesViewModelDelegate {
    var calibrationData = CalibrationData()
        private set

    private var rotationListener: SingleRotationVectorListener = SingleRotationVectorListener(this,
        activity.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    )

    private val searchGameService: BluetoothSearchGameService = BluetoothSearchGameService(activity, this)

    private var deviceList: MutableList<Device> = mutableListOf()

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

    fun scan() {
        searchGameService.scan()
    }

    fun onDestroy() {
        searchGameService.stopScan()
    }

    fun getDeviceList() = deviceList.toList()

    override fun foundDevice(device: Device) {
        val foundDevice = deviceList.find { it.mac == device.mac }
        if(foundDevice != null) {
            val index = deviceList.indexOf(foundDevice)
            deviceList[index] = device
        }
        else {
            deviceList.add(device)
        }
        delegate.foundDevice()
    }

    override fun restartScan() {
        deviceList = mutableListOf()
        delegate.foundDevice()
    }

    override fun joinedGame() {
        delegate.joinedGame()
    }

    fun startGame() {
        searchGameService.stopScan()
    }

    fun joinGame(mac: String) {
        searchGameService.joinGame(mac)
    }
}