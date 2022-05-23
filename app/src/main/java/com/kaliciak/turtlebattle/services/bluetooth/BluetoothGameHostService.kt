package com.kaliciak.turtlebattle.services.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.viewModel.game.GameHostViewModelDelegate
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothGameHostService(
    private val activity: FragmentActivity,
    private val delegate: GameHostViewModelDelegate
) {
    private val REQUEST_ENABLE_BT = 3
    private val charset = Charsets.UTF_8

    private val bluetoothManager: BluetoothManager = activity.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private val uuid = UUID.fromString(activity.resources.getString(R.string.game_service_uuid))
    private var serverSocket: BluetoothServerSocket? = null
    private var socket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var acceptThread: AcceptThread? = null
    private var listenThread: ListenThread? = null
    private var sendThread: SendThread? = null

    private val forResultLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_CANCELED) {
            activity.finish()
        }
        else {
            startServer()
        }
    }

    init {
        getPermissions()
        makeVisible()
    }

    fun sendStartMessage() {
        outputStream?.write(89)
    }

    fun sendBoardData(message: String) {
        sendThread = SendThread(message)
        sendThread?.start()
    }

    fun stop() {
        serverSocket?.close()
        socket?.close()
        listenThread?.stopThread()
    }

    private fun startServer() {
        acceptThread = AcceptThread()
        acceptThread?.start()
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    REQUEST_ENABLE_BT
                )
            }

            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.BLUETOOTH_SCAN),
                    REQUEST_ENABLE_BT
                )
            }

            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.BLUETOOTH_ADVERTISE),
                    REQUEST_ENABLE_BT
                )
            }
        }
        else {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_ENABLE_BT
                )
            }

            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ENABLE_BT
                )
            }
        }
    }

    private fun makeVisible() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        forResultLauncher.launch(discoverableIntent)
    }

    inner class AcceptThread: Thread() {
        init {
            if ( !((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) &&
                (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ))) {
                    serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord("TURTLE BATTLE", uuid)
            }
        }

        override fun run() {
            super.run()
            try {
                socket = serverSocket?.accept()
                inputStream = socket?.inputStream
                outputStream = socket?.outputStream
                delegate.playerConnected()
                Log.d("socket", "acc $serverSocket")
                listenThread = ListenThread()
                listenThread?.start()
            } catch(e: Exception) {
                Log.d("EXCEPTION", "$e")
            }
        }
    }

    inner class ListenThread: Thread() {
        private var stopped = false
            @Synchronized set
            @Synchronized get

        override fun run() {
            super.run()
            try {
                while (!stopped) {
                    val received = ByteArray(1024)
                    val bytes = inputStream?.read(received)
                    if (bytes == null || bytes <= 0) {
                        Log.d("received", "$bytes")
                        Log.d("inpstr", "$inputStream")
                    } else {
                        val str = String(received.copyOfRange(0, bytes), charset)
                        delegate.gotMessage(str)
                    }
                }
            } catch (e: Exception) {
                Log.d("EXCEPTION", "$e")
            }
        }

        fun stopThread() {
            stopped = true
        }
    }

    inner class SendThread(private val message: String): Thread() {
        override fun run() {
            super.run()
            val bytes = message.toByteArray(charset)
            try {
                outputStream?.write(bytes)
            } catch (e: Exception) {
                Log.d("EXCEPTION", "$e")
            }
        }
    }
}