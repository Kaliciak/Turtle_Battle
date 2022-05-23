package com.kaliciak.turtlebattle.services.bluetooth

import android.Manifest
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.kaliciak.turtlebattle.R
import com.kaliciak.turtlebattle.model.bluetooth.Device
import com.kaliciak.turtlebattle.viewModel.foundGames.FoundGamesViewModelDelegate
import java.util.*

class BluetoothSearchGameService(
    private val activity: FragmentActivity,
    private val delegate: FoundGamesViewModelDelegate
) {
    private val bluetoothManager: BluetoothManager = activity.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private val uuid = UUID.fromString(activity.resources.getString(R.string.game_service_uuid))

    // Intent request codes
    private val REQUEST_ENABLE_BT = 3

    private var conThr: ConnectThread? = null

    private var resultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode != RESULT_OK) {
            activity.finish()
        }
    }

    private val deviceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                Log.d("found", "${device}")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                }

                if(device == null) {
                    return
                }
                Log.d("data", "${device.bluetoothClass?.deviceClass} ${intent.getStringExtra(BluetoothDevice.EXTRA_NAME)}")

                if(device.bluetoothClass.deviceClass == BluetoothClass.Device.PHONE_SMART) {
                    val name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME)
                    val mac = device.address
                    val deviceStruct = Device(name ?: "N/A", mac)
                    Log.d("device", "$name, $mac")
                    delegate.foundDevice(deviceStruct)
                }
            }
        }
    }


    init {
        getPermissions()

        // Register for broadcasts when a device is discovered
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity.registerReceiver(deviceReceiver, filter)

        scan()
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
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
                resultLauncher.launch(enableBtIntent)
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

    fun scan() {
        stopScan()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("error", "error")
                return
            }
        }

        delegate.restartScan()
        val result = bluetoothAdapter?.startDiscovery()
        Log.d("s" , "$result")
    }

    fun stopScan() {
        Log.d("scan", "stop")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("error", "error")
                return
            }
        }
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter.cancelDiscovery()
        }
    }

    fun joinGame(mac: String) {
        Log.d("join", mac)
        stopScan()
        Toast.makeText(activity, "Connecting to $mac", Toast.LENGTH_SHORT).show()
        conThr = ConnectThread(mac)
        conThr?.start()
    }

    inner class ConnectThread(private val mac: String): Thread() {
        override fun run() {
            super.run()
            val device = bluetoothAdapter?.getRemoteDevice(mac) ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED ) {
                    return
                }
            }
            try {
                val socket = device.createRfcommSocketToServiceRecord(uuid)
                Log.d("sock", "$socket")
                socket.connect()
                activity.runOnUiThread {
                    Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show()
                }
                BluetoothSavedState.socket = socket
                delegate.joinedGame()
            } catch (e: Exception) {
                activity.runOnUiThread {
                    Toast.makeText(activity, "Couldn't connect", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}