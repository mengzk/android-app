package com.ql.health.ui.fragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ql.health.R
import com.ql.health.custom.VMFragment
import com.ql.health.custom.widget.LoadAnim
import com.ql.health.databinding.FragmentPulseBinding
import com.ql.health.model.body.PulseBody
import com.ql.health.module.common.network.RFCallback
import com.ql.health.module.libs.jtp.JTPulse
import com.ql.health.module.libs.jtp.JTPulse.ResultData
import com.ql.health.module.network.Client
import com.ql.health.module.robot.RobotMsg
import com.ql.health.service.BluetoothScanner
import com.ql.health.util.SheetDialog
import com.ql.health.util.TimerCallback
import com.ql.health.util.TimerUtil

class PulseFragment : VMFragment<FragmentPulseBinding>(R.layout.fragment_pulse) {
    private val TAG = "PulseFragment"
    private lateinit var bluetoothScanner: BluetoothScanner
    private lateinit var pulse: JTPulse
    private var devices: ArrayList<BluetoothDevice> = ArrayList()
    private var headPos = 2 // 手指 2左手 3右手

    @SuppressLint("MissingPermission")
    override fun lazyInit(binding: FragmentPulseBinding) {
        pulse = JTPulse.getInstance(activity)

        connectPulse()

        // 连接失败 -重新连接
        binding.pulseLink.setOnClickListener {
            pulse.stop()
            connectPulse()
        }
        binding.pulseCut.setOnClickListener {
            pulse.stop()
            devices = bluetoothScanner.getDevices()
            if(devices.isEmpty()) {
                onToast("未发现蓝牙设备")
                return@setOnClickListener
            }else if(devices.size == 1) {
                onToast("没有更多设备")
                return@setOnClickListener
            }
            val list = devices.map { it.name } as ArrayList<String>
            SheetDialog.show(activity, list, object : SheetDialog.OnListener {
                override fun onClick(num: Int) {
                    Log.d("SheetDialog", "onSheetClick: $num")
                    pulse.startPulse()
                }
            })
        }

        // 手指选择
        binding.pulseLeft.setOnClickListener {
            headPos = 2
            pulse.startPulse()
        }
        binding.pulseRight.setOnClickListener {
            headPos = 3
            pulse.startPulse()
        }

        activity.findViewById<TextView>(R.id.page_top_title).text = "请将脉诊仪夹到手指上，然后点击开始测量"

        bluetoothScanner = BluetoothScanner(activity)
        bluetoothScanner.start()

        RobotMsg.speech("请将脉诊仪夹到手指上，然后点击开始测量! ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pulse.stopTest()
        bluetoothScanner.stop()
    }

    private fun onLoading() {
        LoadAnim.show(activity, "设备连接中...")
    }

    private fun connectPulse() {
        onLoading()
        pulse.connect(object : JTPulse.OnPulseListener {
            override fun onDevice(device: JTPulse.JTDevice) {
                binding.pulseDeviceName.text = device.name
            }

            override fun onProgress(progress: Int) {
                binding.pulseStatusBox.visibility = View.GONE
                binding.pulseCheckBox.visibility = View.GONE
                binding.pulseDevice.visibility = View.GONE
                binding.pulseProgressBox.visibility = View.VISIBLE
                binding.circularProgress.setProgress(progress)
            }

            override fun onResult(res: String, err: Boolean) {
                Log.i(TAG, "onResult: $res")
                LoadAnim.dismiss()
                binding.pulseStatusBox.visibility = View.VISIBLE
                binding.pulseProgressBox.visibility = View.GONE
                if (err) {
                    binding.pulseStatusIc.setImageResource(R.mipmap.err)
                    binding.pulseLinkOk.visibility = View.GONE
                    binding.pulseLinkFail.visibility = View.VISIBLE

                    binding.pulseCheckBox.visibility = View.GONE
                    binding.pulseLinkBox.visibility = View.VISIBLE
                    binding.pulseDevice.visibility = View.VISIBLE
                } else {
                    binding.pulseStatusIc.setImageResource(R.mipmap.dui)

                    binding.pulseLinkOk.visibility = View.VISIBLE
                    binding.pulseLinkFail.visibility = View.GONE

                    binding.pulseCheckBox.visibility = View.VISIBLE
                    binding.pulseLinkBox.visibility = View.GONE

                    RobotMsg.speech("现在请保持呼吸平稳，放松心情。双手静置于桌面上, 我们的血液在身体里循环一遍，只需要20-50秒 会受到饮食、情绪、运动等因素影响而不断变化。")
                }
            }

            override fun onDown(data: ResultData) {
                // 结束连接 -开始检测舌苔
                binding.pulseProgressBox.visibility = View.GONE
                binding.pulseStatusBox.visibility = View.GONE
                binding.pulseLinkBox.visibility = View.VISIBLE
                binding.pulseDevice.visibility = View.VISIBLE
                onPulseResult(data)
            }
        })
    }

    private fun startPulse() {
        val timerUtil = TimerUtil(20000)
        timerUtil.start(object : TimerCallback {
            override fun update(seconds: Long) {
                if (seconds <= 0) {
                    pulse.startPulse()
                }
            }
        })
    }

    private fun onPulseResult(data: ResultData) {

        LoadAnim.show(activity, "诊断中...")
//        Log.i(TAG, data.dataStr)
        Log.i(
            TAG,
            "ResultData: ${headPos}, ${data.signature}, ${data.mac}, ${data.model}, ${data.rate}, ${data.spo}"
        )

        // 对象值拷贝
        val params = PulseBody(
            data.data,
            data.dataStr,
            data.signature,
            headPos,
            data.mac,
            data.model,
            data.rate,
            data.spo
        )

        Client.main.pulseHealth(params).enqueue(object : RFCallback<String>() {
            override fun onResult(res: String) {
                LoadAnim.dismiss()
//                sendEvent(Bus.Action("pulse", 0, res))
                val bun = Bundle()
                bun.putString("pulse", res)
                navigateTo(R.id.pulse_to_tongue, bun)
            }

            override fun onFail(code: Int, e: Throwable) {
                LoadAnim.dismiss()
                val msg = e.message ?: "脉诊测量失败，请重新测量"
                onToast(msg)
            }
        })
    }

    private fun onToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}