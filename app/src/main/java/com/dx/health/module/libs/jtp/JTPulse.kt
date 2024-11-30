package com.dx.health.module.libs.jtp

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jinmuhealth.bluetooth.session.*
import com.jinmuhealth.bluetooth.session.pulsetest.IPulseTestCallback
import com.jinmuhealth.bluetooth.session.pulsetest.IPulseTestManager
import com.jinmuhealth.bluetooth.session.pulsetest.PulseTestManagerFactory
import com.jinmuhealth.bluetooth.session.pulsetest.PulseTestResult

class JTPulse(private var context: Activity) {
    private val TAG = "JTPulse"
    private val PERMISSION_REQUEST_CODE = 123
    private var deviceScanner: DeviceScanner? = null
    private var deviceSession: DeviceSession? = null
    private var pulseTestManager: IPulseTestManager? = null
    private var isScanning = false;

    // 初始化脉诊仪
    fun connect() {
        if(isScanning) {
            startPulse()
        } else {
            requestStoragePermission()
        }
    }

    fun stop() {
        if(deviceScanner != null) {
            deviceScanner!!.stopScan()
        }
        if (pulseTestManager != null) {
            pulseTestManager!!.stopPulseTest()
        }
    }

    // 开始量测
    private fun startPulse() {
        if (pulseTestManager == null) {
            Toast.makeText(context, "请先连接设备", Toast.LENGTH_SHORT).show()
            return
        }
        pulseTestManager!!.startPulseTest(8000, pulseTestCallback)
    }

    // 检查蓝牙是否开启
    private fun checkBluetoothEnable() {

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter.isEnabled) {
            Log.d(TAG, "蓝牙已开启")
            initBluetooth()
        } else {
            Toast.makeText(context, "请开启蓝牙", Toast.LENGTH_SHORT).show()
        }
    }

    // 检查定位权限是否开启
    private fun checkLocationEnable() {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (locationEnable) {
            checkBluetoothEnable()
        } else {
            Toast.makeText(context, "请打开定位权限", Toast.LENGTH_SHORT).show()
        }
    }

    // 权限申请
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )

        } else {
            // 权限已授权，可以执行相关操作
            checkLocationEnable()
        }
    }

    // 蓝牙初始化
    private fun initBluetooth() {
        val deviceModelList: List<DeviceModel> = arrayListOf(DeviceModel.JM1300)
        deviceScanner = DeviceScanner.create(context, deviceModelList, JTFilter())
        deviceSession = DeviceSession.create(context, DeviceModel.JM1300)
        pulseTestManager = PulseTestManagerFactory.create(deviceSession!!)
        Toast.makeText(context, "开始扫描蓝牙", Toast.LENGTH_SHORT).show()
        deviceScanner!!.startScan(deviceScanCallback)
    }

    // 设备扫描结果回调
    private val deviceScanCallback: IDeviceScanCallback = object : IDeviceScanCallback {

        override fun onDeviceDiscovered(device: Device) {
            context.runOnUiThread {
                Toast.makeText(context, "扫描到脉诊仪蓝牙设备" + device.name, Toast.LENGTH_SHORT)
                    .show()
                deviceScanner!!.stopScan()
                Toast.makeText(context, "开始连接脉诊仪蓝牙", Toast.LENGTH_SHORT).show()
                deviceSession!!.connect(device, connectDeviceCallback)
            }
        }
    }

    // 设备连接结果回调
    private val connectDeviceCallback: IConnectDeviceCallback = object : IConnectDeviceCallback {

        override fun disFirmwareRev(firmwareRev: String?) {}
        override fun disRssi(rssi: Int?) {}

        override fun onConnectionStateChange(newState: Int) {
            context.runOnUiThread {
                when (newState) {
                    ConnectionState.STATE_CONNECTED -> {}
                    ConnectionState.STATE_DISCONNECTING, ConnectionState.STATE_DISCONNECTED -> {
                        isScanning = false
                    }
                    ConnectionState.STATE_READY -> {
                        if (pulseTestManager != null) {
                            isScanning = true
                            Toast.makeText(context, "连接脉诊仪成功", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    // 数据采集结果回调
    private val pulseTestCallback: IPulseTestCallback = object : IPulseTestCallback {

        override fun onPulseProgressUpdate(pulseProgress: Int) {
            Log.d(TAG, "采集数据中...$pulseProgress")
            context.runOnUiThread {
                if (pulseProgress == 100) {
                    Toast.makeText(context, "采集数据中完成", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onDone(pulseTestResult: PulseTestResult) {
            Log.d(TAG, "采集数据完成：${pulseTestResult.data.contentToString()}")

            val signature = MD5Util().getMD5String(pulseTestResult.data)
            val data = Base64.encodeToString(pulseTestResult.data, Base64.DEFAULT)
            Log.d(TAG, "--------->：$signature")
            Log.d(TAG, "--------->：$data")
        }

        override fun onError(error: ErrorType) {
            Log.d(TAG, "--------->：${error.errorMsg}")
            context.runOnUiThread {
                Toast.makeText(context, "采集数据出现错误", Toast.LENGTH_SHORT).show()
                if (error == ErrorType.EXCEPTION_PULSE_DATA_IS_OUT_OF_EXPECTED_NUMBER) {
                    Toast.makeText(context, "请将手指置入脉诊仪", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "采集数据出现错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}