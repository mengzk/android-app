package com.ql.health.module.libs.jtp

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.CountDownTimer
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
    private val PERMISSION_CODE = 123
    private var deviceScanner: DeviceScanner? = null
    private var deviceSession: DeviceSession? = null
    private var pulseTestManager: IPulseTestManager? = null
    private var pulseListener: OnPulseListener? = null
    private var curDevice: Device? = null
    private lateinit var countDownTimer: CountDownTimer
    private var isScanning = false
    private var isPulse = false

    // 初始化脉诊仪
    fun connect(listener: OnPulseListener) {
        pulseListener = listener
        if (isScanning) {
            startPulse()
        } else {
            requestStoragePermission()
        }
    }

    fun stop() {
        if (deviceScanner != null) {
            deviceScanner!!.stopScan()
        }
        if (deviceSession != null) {
            deviceSession!!.disconnect()
            deviceSession!!.release()
        }
        if (pulseTestManager != null) {
            pulseTestManager!!.stopPulseTest()
        }
        isPulse = false
    }

    // 开始量测
    fun startPulse() {
        if (pulseTestManager == null) {
            onToast("请先连接设备")
            return
        }
        if (isPulse) {
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
            onToast("请开启蓝牙")
            pulseListener?.onResult("请开启蓝牙", true)
        }
    }

    // 检查定位权限是否开启
    private fun checkLocationEnable() {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (locationEnable) {
            checkBluetoothEnable()
        } else {
            onToast("请打开定位权限")
            pulseListener?.onResult("请打开定位权限", true)
        }
    }

    // 权限申请
    private fun requestStoragePermission() {
        val LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        val permission = ContextCompat.checkSelfPermission(context, LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, arrayOf(LOCATION), PERMISSION_CODE)
            pulseListener?.onResult("请打开蓝牙，位置权限", true)
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
//        Toast.makeText(context, "开始扫描蓝牙", Toast.LENGTH_SHORT).show()
        deviceScanner!!.startScan(deviceScanCallback)
        // 倒计时 30s 后停止扫描
        startCountdownTimer(10000)
    }

    private fun startCountdownTimer(millis: Long) {
        countDownTimer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (deviceScanner != null) {
                    deviceScanner!!.stopScan()
                }
                isPulse = false
                onToast("未扫描到脉诊仪设备")
                pulseListener?.onResult("未扫描到脉诊仪设备", true)
                countDownTimer.cancel()
            }
        }.start()
    }

    // 设备扫描结果回调
    private val deviceScanCallback: IDeviceScanCallback = object : IDeviceScanCallback {

        override fun onDeviceDiscovered(device: Device) {
            Log.d(TAG, "扫描到蓝牙设备：${device.name}")
            context.runOnUiThread {
                if (device.name == null) {
                    pulseListener?.onResult("未扫描到脉诊仪设备", true)
                } else {
                    countDownTimer.cancel()
                    deviceScanner!!.stopScan()
//                    onToast("开始连接脉诊仪蓝牙")
                    curDevice = device
                    deviceSession!!.connect(device, connectDeviceCallback)
                }
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
                        isPulse = false
                        pulseListener?.onResult("连接脉诊仪失败", true)
                    }

                    ConnectionState.STATE_READY -> {
                        if (pulseTestManager != null) {
                            isScanning = true
//                            onToast("连接脉诊仪成功")
                            pulseListener?.onResult("连接脉诊仪成功", false)
                        }
                    }
                }
            }
        }
    }

    // 数据采集结果回调
    private val pulseTestCallback: IPulseTestCallback = object : IPulseTestCallback {
        var count = 0
        override fun onPulseProgressUpdate(pulseProgress: Int) {
            if (count == pulseProgress) {
                return;
            }
            count = pulseProgress
            Log.d(TAG, "------> 采集中...$pulseProgress")
            isPulse = true
            context.runOnUiThread {
                if (pulseProgress <= 100) {
                    pulseListener?.onProgress(pulseProgress)
                }
            }
        }

        override fun onDone(pulseTestResult: PulseTestResult) {
            // 转成 1,2,3,2
            Log.d(TAG, "采集数据完成：${pulseTestResult.data.contentToString()}")
            val rate = pulseTestResult.avgHeartRate
            val spo = pulseTestResult.spo
            val signature = MD5Util().getMD5String(pulseTestResult.data)
            val str = Base64.encodeToString(pulseTestResult.data, Base64.DEFAULT)
            context.runOnUiThread {
                if (curDevice != null) {
                    var mac = (curDevice!!.mac ?: "").split(":").joinToString("") { it }
//                    if(mac.length >= 12) {
//                        mac = mac.substring(6, 12)
//                    }
//                    val mac = curDevice!!.mac ?: ""
                    val model = curDevice!!.deviceModel ?: ""
                    val result = ResultData(pulseTestResult.data, str, signature, mac, model, rate, spo)
                    pulseListener?.onDown(result)
                }
            }
            isPulse = false
        }

        override fun onError(error: ErrorType) {
            Log.d(TAG, "------>：${error.errorMsg}")
            isPulse = false
            context.runOnUiThread {
                if (error == ErrorType.EXCEPTION_PULSE_DATA_IS_OUT_OF_EXPECTED_NUMBER) {
                    onToast("请将手指置入脉诊仪")
                    pulseListener?.onResult("请将手指置入脉诊仪", true)
                } else {
                    onToast("采集数据出现错误")
                    pulseListener?.onResult("采集数据出现错误", true)
                }
            }
        }
    }

    private fun onToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    interface OnPulseListener {
        fun onProgress(progress: Int)
        fun onResult(res: String, err: Boolean)
        fun onDown(data: ResultData)
    }

    data class ResultData(val data: ByteArray, val dataStr: String, val signature: String, val mac: String, val model: String, val rate: Int, val spo: Int) {}
}