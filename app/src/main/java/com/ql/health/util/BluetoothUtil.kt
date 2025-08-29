package com.ql.health.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

/**
 * Author: Meng
 * Date: 2025/01/09
 * Modify: 2025/01/09
 * Desc:
 */
object BluetoothUtil {

    @SuppressLint("MissingPermission")
    fun getList(): List<BluetoothDevice> {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        return pairedDevices?.toList() ?: emptyList()
    }

    @SuppressLint("MissingPermission")
    fun getNames(): List<String> {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        return pairedDevices?.map { it.name } ?: emptyList()
    }

}