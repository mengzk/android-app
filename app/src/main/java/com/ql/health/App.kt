package com.ql.health

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.HandlerThread
import android.util.Log
import com.ainirobot.lib.shadowopk.AbsRobotMessenger
import com.ainirobot.lib.shadowopk.RobotMessengerCallBack
import com.ainirobot.lib.shadowopk.RobotMessengerManager.connectRobot
import com.ainirobot.lib.shadowopk.RobotMessengerManager.setRobotMessenger
import com.ql.health.config.Consts
import com.ql.health.custom.AppExceptionHandler
import com.ql.health.module.robot.MRobotMessenger
import com.ql.health.module.robot.RobotMsg
import com.ql.health.module.robot.SpeechCallback
import com.tencent.bugly.crashreport.CrashReport


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        myContext = this
//        AppExceptionHandler(this).init()

        CrashReport.initCrashReport(applicationContext, "645ca5dbef", true)
        initRobotApi()
    }

    // 初始化机器人API
    private fun initRobotApi() {
        val context = this
        val callbackThread = HandlerThread("RobotOSDemo")
        callbackThread.start()
        CrashReport.setUserId(Consts.getDeviceName())
        connectRobot(context, object : RobotMessengerCallBack {
                override fun onMessengerReady() {
                    Log.i("SHADOW_OPK", "RobotMessengerCallBack ---> onMessengerReady")
                    val mrm = MRobotMessenger(context)
                    mrm.setRobotCallback(object : MRobotMessenger.RobotCallback {
                        override fun onResult(result: String?) {
                            Log.i("SHADOW_OPK", "client onRobotMessage 5: $result")
                            CrashReport.setAppChannel(context, result)
                        }
                    })
                    setRobotMessenger(mrm)

                    RobotMsg.getRobotSn()
//                    CrashReport.setDeviceId()
                }
            }
        )

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var myContext: Context
        fun getContext(): Context {
            return myContext
        }
    }

}