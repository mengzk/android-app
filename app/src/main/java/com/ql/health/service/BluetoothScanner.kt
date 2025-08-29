package com.ql.health.service

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

/**
 * Author: Meng
 * Date: 2025/01/09
 * Modify: 2025/01/09
 * Desc:
 */

class BluetoothScanner(private val context: Context) {
    private val TAG = "BluetoothDeviceScanner"

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val discoveredDevices: ArrayList<BluetoothDevice> = ArrayList()

    private val receiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    if(device.name != null && device.name.startsWith("JT")) {
                        discoveredDevices.add(it)
                        Log.i(TAG, "-----> device: ${it.name} - ${it.address}")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun start() {
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter.cancelDiscovery()
        }
        discoveredDevices.clear()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
        bluetoothAdapter?.startDiscovery()
    }

    /**
     * 停止扫描 Discovery
     */
    @SuppressLint("MissingPermission")
    fun stop() {
        bluetoothAdapter?.cancelDiscovery()
        context.unregisterReceiver(receiver)
    }

    fun getDevices(): ArrayList<BluetoothDevice> {
        return discoveredDevices
    }

}