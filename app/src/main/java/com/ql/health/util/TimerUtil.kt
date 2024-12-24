package com.ql.health.util

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
    private var count = millis

    fun start(call: TimerCallback) {
        millis = count
        countDownTimer = object : CountDownTimer(millis, 1000) {
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
        val minutes = (millis / 1000).toInt() / 60
        val seconds = (millis / 1000) % 60
        call.update(seconds)
        val timeStr = String.format("%02d:%02d", minutes, seconds)
        Log.i("TimerUtil", "Time left: $timeStr")
    }

    fun stop() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }
}

interface TimerCallback {
    fun update(seconds: Long)
}