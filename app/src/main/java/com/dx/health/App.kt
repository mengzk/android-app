package com.dx.health

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.dx.health.custom.AppExceptionHandler

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        myContext = this
        AppExceptionHandler(this).init()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var myContext: Context
        fun getContext(): Context {
            return myContext
        }
    }

}