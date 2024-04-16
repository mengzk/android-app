package com.dxm.robotchat.modules.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Meng
 * Date: 2023/05/10
 * Desc:
 */
public class BluetoothService extends Service {

    private static final String TAG = "BluetoothService";
    private static BluetoothService instance;



    // 服务相关
    private final IBinder mBinder = new LocalBinder();
    private BluetoothAdapter blaAdapter;
    private BluetoothGatt blaGatt;
    private ArrayList<BluetoothDevice> bleDeviceList= new ArrayList<>();

    private ExecutorService executorService;
    private ServiceHandler serviceHandler;
    private Looper serviceLooper;
    private int PRIORITY_CODE = 321123;
    private final UUID SERVICE_UUID = UUID.fromString("0000gss0-0000-3000-9000-00230k9s76fg");
    // 蓝牙已连接
    public final static String ACTION_GATT_CONNECTED = "com.mon.qinglan.ACTION_GATT_CONNECTED";
    // 蓝牙已断开
    public final static String ACTION_GATT_DISCONNECTED = "com.mon.qinglan.ACTION_GATT_DISCONNECTED";
    // 发现GATT服务
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.mon.qinglan.ACTION_GATT_SERVICES_DISCOVERED";
    // 收到蓝牙数据
    public final static String ACTION_DATA_AVAILABLE = "com.mon.qinglan.ACTION_DATA_AVAILABLE";
    // 连接失败
    public final static String ACTION_CONNECTING_FAIL = "com.mon.qinglan.ACTION_CONNECTING_FAIL";
    // 蓝牙数据
    public final static String EXTRA_DATA = "com.mon.qinglan.EXTRA_DATA";

    private  BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int arg1, byte[] arg2) {
            if(device != null) {
                Log.i(TAG, device.getAddress());
            }
        }
    };
    /**
     * 蓝牙操作回调
     * 蓝牙连接状态才会回调
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 蓝牙已连接
                sendBleBroadcast(ACTION_GATT_CONNECTED);
                // 搜索GATT服务
                blaGatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // 蓝牙已断开连接
                sendBleBroadcast(ACTION_GATT_DISCONNECTED);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            // 发现GATT服务
            if (status == BluetoothGatt.GATT_SUCCESS) {
                setBleNotification();
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            // 收到数据
            sendBleBroadcast(ACTION_DATA_AVAILABLE, characteristic);
        }
    };


    public static BluetoothService getInstance() {
        if (instance == null) {
            synchronized (BluetoothService.class) {
                if (instance == null) {
                    instance = new BluetoothService();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("BluetoothService", PRIORITY_CODE);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

        executorService = Executors.newSingleThreadExecutor();

        //获取系统蓝牙适配器管理类
        blaAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = serviceHandler.obtainMessage();
        if (intent.hasExtra("bluetooth")) {
            msg.what = 1;
        } else if (intent.hasExtra("scan")) {
            msg.what = 2;
        } else if (intent.hasExtra("contact")) {
            msg.what = 3;
        } else {
            msg.arg1 = startId;
        }
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    /**
     * 发送通知
     *
     * @param action 广播Action
     */
    private void sendBleBroadcast(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    /**
     * 发送通知
     *
     * @param action         广播Action
     * @param characteristic 数据
     */
    private void sendBleBroadcast(String action, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        if (SERVICE_UUID.equals(characteristic.getUuid())) {
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
        }
        sendBroadcast(intent);
    }


    public void scanDevice() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @SuppressLint("MissingPermission")
    public void linkDevice(int position) {
        blaAdapter.stopLeScan(scanCallback);
        String address = bleDeviceList.get(position).getAddress();

        if (blaAdapter == null || TextUtils.isEmpty(address)) {
            return;
        }

        BluetoothDevice device = blaAdapter.getRemoteDevice(address);
        if (device == null) {
            return;
        }
        blaGatt = device.connectGatt(this, false, mGattCallback);
    }

    @SuppressLint("MissingPermission")
    private void startScan() {
        blaAdapter.stopLeScan(scanCallback);
        blaAdapter.startLeScan(scanCallback);
        Message msg = serviceHandler.obtainMessage();
        msg.what = 5;
        serviceHandler.sendMessage(msg);
    }


    /**
     * 设置蓝牙设备在数据改变时，通知App
     */
    @SuppressLint("MissingPermission")
    public void setBleNotification() {
        if (blaGatt == null) {
            sendBleBroadcast(ACTION_CONNECTING_FAIL);
            return;
        }

        // 获取蓝牙设备的服务
        BluetoothGattService gattService = blaGatt.getService(SERVICE_UUID);
        if (gattService == null) {
            sendBleBroadcast(ACTION_CONNECTING_FAIL);
            return;
        }

        // 获取蓝牙设备的特征
        BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(SERVICE_UUID);
        if (gattCharacteristic == null) {
            sendBleBroadcast(ACTION_CONNECTING_FAIL);
            return;
        }

        // 获取蓝牙设备特征的描述符
        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(SERVICE_UUID);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        if (blaGatt.writeDescriptor(descriptor)) {
            // 蓝牙设备在数据改变时，通知App，App在收到数据后回调onCharacteristicChanged方法
            blaGatt.setCharacteristicNotification(gattCharacteristic, true);
        }
    }

    /**
     * 发送数据
     *
     * @param data 数据
     * @return true：发送成功 false：发送失败
     */
    @SuppressLint("MissingPermission")
    public boolean sendData(byte[] data) {
        // 获取蓝牙设备的服务
        BluetoothGattService gattService = null;
        if (blaGatt != null) {
            gattService = blaGatt.getService(SERVICE_UUID);
        }
        if (gattService == null) {
            return false;
        }

        // 获取蓝牙设备的特征
        BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(SERVICE_UUID);
        if (gattCharacteristic == null) {
            return false;
        }

        // 发送数据
        gattCharacteristic.setValue(data);
        return blaGatt.writeCharacteristic(gattCharacteristic);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // 蓝牙断开连接
    @SuppressLint("MissingPermission")
    public void disconnect() {
        if (blaGatt != null) {
            blaGatt.disconnect();
        }
    }

    // 释放相关资源
    @SuppressLint("MissingPermission")
    public void dispose() {
        if (blaGatt != null) {
            blaGatt.close();
        }
    }


    // Handler that receives messages from the thread
    final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 5) {
                    blaAdapter.stopLeScan(scanCallback);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }



    public class LocalBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }
}
