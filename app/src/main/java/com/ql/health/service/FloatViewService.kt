package com.ql.health.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.ql.health.MainActivity
import com.ql.health.R
import com.ql.health.custom.ActivityStack

class FloatViewService  : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingButton: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatingButton = LayoutInflater.from(this).inflate(R.layout.float_layout, null, false)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.BOTTOM or Gravity.END
        params.y = 300
//        params.x = 0
//        params.y = 0

        windowManager.addView(floatingButton, params)

        val button = floatingButton.findViewById<ImageView>(R.id.float_home)
        button.setOnClickListener {
            // Handle button click
            openHomePage()
        }

        println("----------------- FloatViewService.onCreate() -----------------")
    }

    private fun openHomePage() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        ActivityStack.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingButton.isInitialized) {
            windowManager.removeView(floatingButton)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}