package com.ql.health.module.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.TextView
import com.ql.health.R
import com.ql.health.config.Configs
import com.ql.health.custom.ActivityStack
import com.ql.health.module.common.Log3

/**
 * Author: Meng
 * Date: 2023/07/05
 * Desc:
 */
class LogService : Service() {
    private lateinit var windowManager: WindowManager
    private var floatView: View? = null // 悬浮窗View

    companion object {
        fun openLog() {
            val activity = ActivityStack.peek()
            val intent = Intent(activity, LogService::class.java)
            activity.startService(intent)
        }

        fun closeLog() {
            val activity = ActivityStack.peek()
            val intent = Intent(activity, LogService::class.java)
            activity.stopService(intent)
            Log3.i("", "------> closeLog")
        }
    }

    override fun onCreate() {
        super.onCreate()
        showWindow()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (floatView != null) {
            floatView?.findViewById<TextView>(R.id.app_debug_status)?.text = Configs.getEnv()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showWindow() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//        val outMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(outMetrics)
        val layoutParam = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
//            format = PixelFormat.RGBA_8888
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            //位置大小设置
            width = WRAP_CONTENT
            height = WRAP_CONTENT
            gravity = Gravity.RIGHT or Gravity.TOP
            //设置剧中屏幕显示
//            x = 0;y = 0
        }
        // 新建悬浮窗控件
        floatView = LayoutInflater.from(this).inflate(R.layout.app_log_layout, null)
        floatView?.setOnClickListener {
            // 点击悬浮窗控件时，跳转到主界面
        }
        floatView?.findViewById<TextView>(R.id.app_debug_status)?.text = Configs.getEnv()
        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(floatView, layoutParam)
//        windowManager?.removeView(floatView)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        if (floatView != null) {
            windowManager.removeView(floatView)
        }
    }

}