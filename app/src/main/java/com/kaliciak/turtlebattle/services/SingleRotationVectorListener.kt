package com.kaliciak.turtlebattle.services

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper

class SingleRotationVectorListener(private val delegate: RotationVectorListenerDelegate,
                                   private val sensorManager: SensorManager)
    : SensorEventListener {

    private var accelerometerChecked = false
        @Synchronized get
        @Synchronized set
    private var magneticFieldChecked = false
        @Synchronized get
        @Synchronized set
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    fun getData() {
        accelerometerChecked = false
        magneticFieldChecked = false
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
            accelerometerChecked = true
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            magneticFieldChecked = true
        }

        if(accelerometerChecked and magneticFieldChecked) {
            computeOrientation()
            notifyDelegate()
            sensorManager.unregisterListener(this)
        }
    }

    private fun computeOrientation() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }

    private fun notifyDelegate() {
        delegate.didChange(
            orientationAngles[2],
            orientationAngles[1],
            orientationAngles[0]
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        Log.d("listener", "onAccuracyChanged")
    }
}