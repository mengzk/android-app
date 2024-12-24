package com.ql.health.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ql.health.R
import com.ql.health.custom.AppFragment
import com.ql.health.custom.widget.LoadAnim
import com.ql.health.databinding.FragmentPulseBinding
import com.ql.health.model.body.PulseBody
import com.ql.health.module.common.network.RFCallback
import com.ql.health.module.event.Bus
import com.ql.health.module.libs.jtp.JTPulse
import com.ql.health.module.libs.jtp.JTPulse.ResultData
import com.ql.health.module.network.Client
import com.ql.health.util.TimerCallback
import com.ql.health.util.TimerUtil

class PulseFragment : AppFragment<FragmentPulseBinding>(R.layout.fragment_pulse) {
    private val TAG = "PulseFragment"
    private lateinit var pulse: JTPulse
    private var headPos = 1 // 手指 1左手 2右手

    override fun onBindView(binding: FragmentPulseBinding, savedInstanceState: Bundle?) {
        super.onBindView(binding, savedInstanceState)
        this.binding = binding
    }

    override fun lazyInit(binding: FragmentPulseBinding) {
        pulse = JTPulse(activity)
        connectPulse()

        // 连接失败 -重新连接
        binding.pulseReconnection.setOnClickListener {
            connectPulse()
        }
        binding.pulseCutHand.setOnClickListener {
            headPos = headPos % 2
            val str = if (headPos == 1) "左手" else "右手"
            val str2 = if (headPos == 1) "右手" else "左手"
            binding.pulseHint.text = "请将脉诊仪夹到${str}食指上, 点击"
            binding.pulseCutHand.text = "切换到$str2"
            pulse.stop()
            connectPulse()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pulse.stop()
    }

    private fun connectPulse() {
        binding.pulseStatusText.text = "正在连接脉诊仪..."
        pulse.connect(object : JTPulse.OnPulseListener {
            override fun onProgress(progress: Int) {
//                binding.pulseSyncLink.visibility = View.GONE
//                binding.pulseStatusNum.visibility = View.VISIBLE
                binding.pulseStatusText.text = "脉诊测量中..."
                binding.pulseStatusNum.text = "$progress%"
            }

            override fun onResult(res: String, err: Boolean) {
                binding.pulseStatusText.text = res
                Log.i(TAG, "onResult: $res")
                if (err) {
                    // 连接失败 -重新连接
                    binding.pulseReconnection.visibility = View.VISIBLE
                } else {
                    // 连接成功 -开始检测舌苔
                    binding.pulseReconnection.visibility = View.GONE
                    binding.pulseStatusNum.visibility = View.VISIBLE
                    startPulse()
                }
            }

            override fun onDown(data: ResultData) {
                val batchSize = 200 // 每批打印的大小
                // 结束连接 -开始检测舌苔
                try {
                    binding.pulseStatusText.text = "脉诊测量成功"
                    binding.pulseStatusNum.visibility = View.GONE
                }catch (e: Exception) {
                    e.printStackTrace()
                }
                onPulseResult(data)
            }
        })
    }

    private fun startPulse() {
        val timerUtil = TimerUtil(1200)
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
        Log.i(TAG, data.dataStr)
        Log.i(TAG, "ResultData: ${headPos}, ${data.signature}, ${data.mac}, ${data.model}, ${data.rate}, ${data.spo}")

        // 对象值拷贝
        val params = PulseBody(data.data, data.dataStr, data.signature, headPos+2, data.mac, data.model, data.rate,data.spo)

        Client.main.pulseHealth(params).enqueue(object : RFCallback<String>() {
            override fun onResult(res: String) {
                LoadAnim.dismiss()
                Bus.send("check-event", Bus.Action("pulse", 0, res))
            }

            override fun onFail(code: Int, e: Throwable) {
                LoadAnim.dismiss()
                onToast("脉诊测量失败，请重新测量")
            }
        })
    }

    private fun onToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
}