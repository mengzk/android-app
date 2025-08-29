package com.ql.health.module.robot

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.ainirobot.lib.shadowopk.AbsRobotMessenger
import com.ql.health.custom.ActivityStack

class MRobotMessenger(var context: Context) : AbsRobotMessenger() {
    private var handler: Handler = Handler(Looper.getMainLooper())
    var callback: RobotCallback? = null

    init {
        Log.i("SHADOW_OPK", "client init")
        MRobotMessenger.instance = this
    }

    fun setRobotCallback(callback: RobotCallback?) {
        this.callback = callback
    }

    override fun onRobotMessage(message: String?) {
        Log.i("SHADOW_OPK", "client onRobotMessage 2: $message")
        handler.post(Runnable {
            val activity: Activity = ActivityStack.getTop()
            Log.i("SHADOW_OPK", "client onRobotMessage获取内容: $message")
            if (callback != null) {
                Log.i("SHADOW_OPK", "------> client onRobotMessage ")
                callback!!.onResult(message)
            }
        })
    }

    interface RobotCallback {
        fun onResult(result: String?)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: MRobotMessenger? = null

        fun getInstance(): MRobotMessenger {
            return instance!!
        }
    }
}