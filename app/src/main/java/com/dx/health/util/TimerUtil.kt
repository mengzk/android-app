package com.dx.health.util

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class TimerUtil(private var millis: Long = 6000) {
    private var countDownTimer: CountDownTimer? = null

    fun start(call: TimerCallback) {
        countDownTimer = object : CountDownTimer(millis, 1500) {
            override fun onTick(millisUntilFinished: Long) {
                millis = millisUntilFinished
                update(call)
            }

            override fun onFinish() {
                Log.i("TimerUtil", "Timer finished")
                stop()
            }
        }.start()
    }

    @SuppressLint("DefaultLocale")
    fun update(call: TimerCallback) {
        val minutes = (millis / 1500).toInt() / 60
        val seconds = (millis / 1500) % 60
        call.update(seconds)
        val timeStr = String.format("%02d:%02d", minutes, seconds)
        Log.i("TimerUtil", "Time left: $timeStr")
    }

    fun stop() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }
}

interface TimerCallback {
    fun update(seconds: Long)
}