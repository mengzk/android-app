package com.ql.health.service

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class BluetService : Service() {
    private val MESSAGE_READ = 10
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val SERVICE_CODE = 1230
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var taskThread: Executor? = null

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null

    // Handler that receives messages from the thread
    private class ServiceHandler(looper: Looper?) : Handler(looper!!) {
        override fun handleMessage(msg: Message) {
            if (msg.what == 10) {
                val readBuf = msg.obj as ByteArray
                val receivedMessage = String(readBuf, 0, msg.arg1, StandardCharsets.UTF_8)
                Log.i("ServiceHandler", "Received: $receivedMessage")
                // Process the received message as needed
            }
            //            Thread.sleep(1000);
//            Thread.currentThread().interrupt();
//            stopSelf(msg.arg1);
        }
    }

    /**
     * 首次创建服务时，系统会（在调用 onStartCommand() 或 onBind() 之前）调用此方法来执行一次性设置程序。如果服务已在运行，则不会调用此方法。
     */
    override fun onCreate() {
        super.onCreate()
        taskThread = Executors.newSingleThreadExecutor()

        val thread = HandlerThread("BleLinkService", SERVICE_CODE)
        thread.start()

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.looper
        serviceHandler = ServiceHandler(serviceLooper)
    }

    /**
     * 当另一个组件（如 Activity）请求启动服务时，系统会通过调用 startService() 来调用此方法。
     * 执行此方法时，服务即会启动并可在后台无限期运行。
     * 如果您实现此方法，则在服务工作完成后，您需负责通过调用 stopSelf() 或 stopService() 来停止服务。如果您只想提供绑定，则无需实现此方法。
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        /**
         * START_NOT_STICKY
         * 如果系统在 onStartCommand() 返回后终止服务，则除非有待传递的挂起 Intent，否则系统不会重建服务。这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。
         * START_STICKY
         * 如果系统在 onStartCommand() 返回后终止服务，则其会重建服务并调用 onStartCommand()，但不会重新传递最后一个 Intent。相反，除非有挂起 Intent 要启动服务，否则系统会调用包含空 Intent 的 onStartCommand()。在此情况下，系统会传递这些 Intent。此常量适用于不执行命令、但无限期运行并等待作业的媒体播放器（或类似服务）。
         * START_REDELIVER_INTENT
         * 如果系统在 onStartCommand() 返回后终止服务，则其会重建服务，并通过传递给服务的最后一个 Intent 调用 onStartCommand()。所有挂起 Intent 均依次传递。此常量适用于主动执行应立即恢复的作业（例如下载文件）的服务。
         */
//        return super.onStartCommand(intent, flags, startId);
        val msg: Message = serviceHandler!!.obtainMessage()
        msg.arg1 = startId
        serviceHandler!!.sendMessage(msg)

        val device = intent.getParcelableExtra<BluetoothDevice>("device")
        if (device != null) {
            connectToDevice(device)
        }

        return START_STICKY
    }

    /**
     * 当另一个组件想要与服务绑定（例如执行 RPC）时，系统会通过调用 bindService() 来调用此方法。
     * 在此方法的实现中，您必须通过返回 IBinder 提供一个接口，以供客户端用来与服务进行通信。请务必实现此方法；但是，如果您并不希望允许绑定，则应返回 null。
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    /**
     * 当不再使用服务且准备将其销毁时，系统会调用此方法。服务应通过实现此方法来清理任何资源，如线程、注册的侦听器、接收器等。这是服务接收的最后一个调用。
     */
    override fun onDestroy() {
        super.onDestroy()
    }


    @SuppressLint("MissingPermission")
    private fun initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show()
        } else if (!bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        } else {
            listPairedDevices()
        }
    }

    @SuppressLint("MissingPermission")
    private fun listPairedDevices() {
        val pairedDevices = bluetoothAdapter!!.bondedDevices
        if (!pairedDevices.isEmpty()) {
            for (device in pairedDevices) {
                Log.i("Bluetooth", "Paired Device: " + device.name + " - " + device.address)
                if (device.name == "S21") {
//                    connectToDevice(device);
                }
            }
            //            connectToDevice(pairedDevices.iterator().next());
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        Log.i("BleLinkService", device.name + " - " + device.address)
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
            bluetoothSocket!!.connect()

            val connectedThread: ConnectedThread = ConnectedThread(bluetoothSocket!!)

            taskThread!!.execute(connectedThread)
            //            connectedThread.start();
        } catch (e: IOException) {
            Log.e("TAG", "Error connecting to device", e)
            try {
                bluetoothSocket!!.close()
            } catch (closeException: IOException) {
                Log.e("TAG", "Could not close the client socket", closeException)
            }
        }
    }


    private inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {
        private val inStream: InputStream?
        private val outStream: OutputStream?

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null

            try {
                tmpIn = socket.inputStream
                tmpOut = socket.outputStream
            } catch (e: IOException) {
                Log.e("ConnectedThread", "Error occurred when creating input/output stream", e)
            }

            inStream = tmpIn
            outStream = tmpOut
        }

        override fun run() {
            val buffer = ByteArray(1024)
            var numBytes: Int
            var msg = "Hello, I am Android"
            while (true) {
                try {
                    numBytes = inStream!!.read(buffer)
                    // Handle the received data
                    val readMsg: Message =
                        serviceHandler!!.obtainMessage(MESSAGE_READ, numBytes, -1, buffer)
                    readMsg.sendToTarget()
                    try {
                        sleep(2000)
                    } catch (e: InterruptedException) {
                        throw RuntimeException(e)
                    }
                    msg = "time:" + System.currentTimeMillis()
                    write(msg.toByteArray())
                } catch (e: IOException) {
                    Log.d("ConnectedThread", "Input stream was disconnected", e)
                    break
                }
            }
        }

        fun write(bytes: ByteArray?) {
            try {
                outStream!!.write(bytes)
            } catch (e: IOException) {
                Log.e("ConnectedThread", "Error occurred when sending data", e)
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: IOException) {
                Log.e("ConnectedThread", "Could not close the connect socket", e)
            }
        }
    }
}