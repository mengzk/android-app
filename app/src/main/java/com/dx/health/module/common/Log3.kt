package com.dx.health.module.common

import android.util.Log

/**
 * Author: Meng
 * Date: 2024/11/20
 * Modify: 2024/11/20
 * Desc: 日志帮助类
 */

object Log3 {
    private const val tag3 = "LogDev"

    fun i(tag: String?, msg: String) {
        Log.i(tag, msg)
    }

    fun d(tag: String?, msg: String) {
        Log.d(tag, msg)
    }

    fun e(tag: String?, msg: String) {
        Log.e(tag, msg)
    }

    fun v(tag: String?, msg: String) {
        Log.v(tag, msg)
    }

    fun w(tag: String?, msg: String) {
        Log.w(tag, msg)
    }

    fun s(tag: String?, msg: String) {
        Log.d(tag, msg)
    }

    fun u(tag: String?, msg: String) {
        Log.d(tag, msg)
    }

    fun getStackTraceString(ex: Throwable): String {
        return ""
    }
}