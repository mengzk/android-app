package com.dxm.aimodel.ui.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.dxm.aimodel.R;
import com.dxm.aimodel.base.AppActivity;
import com.dxm.aimodel.service.BleLinkService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BleActivity extends AppActivity {
    private static final String TAG = "BluActivity";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBluetooth();
    }

    private void initView() {
//        connectToDevice(null);
    }

    @SuppressLint("MissingPermission")
    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            listPairedDevices();
        }
    }

    @SuppressLint("MissingPermission")
    private void listPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (!pairedDevices.isEmpty()) {
            for (BluetoothDevice device : pairedDevices) {
                Log.i("BleActivity", "Paired Device: " + device.getName() + " - " + device.getAddress());
                if(device.getName().equals("S21")) {
                    connectToDevice(device);
                }
            }
//            connectToDevice(pairedDevices.iterator().next());
        }
    }

    @SuppressLint("MissingPermission")
    private void connectToDevice(BluetoothDevice device) {
        Intent intent = new Intent(this, BleLinkService.class);
        intent.putExtra("device", device);
        startService(intent);
    }

    // 获取拍照结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                listPairedDevices();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}