package com.dx.health.module.common.log

import android.util.Log
import com.google.gson.Gson

/**
 * Author: Meng
 * Date: 2023/07/21
 * Modify: 2023/07/21
 * Desc: 日志存储
 */
class LogStore {
    private var logListener: OnLogListener? = null
    var logList = ArrayList<LogDao>()

    companion object {
        var logStore: LogStore? = null

        fun enable() {
            logStore = LogStore()
        }

        fun disable() {
            clear()
            logStore = null
        }

        fun add(log: LogDao) {
            logStore?.add(log)
        }

        fun add(
            url: String,
            method: String,
            code: Int,
            time: Long,
            params: String,
            headers: Map<String, Any>,
            result: String?
        ) {
            val duration = ((System.nanoTime() - time) / 1e6).toLong()
            add(LogDao(url, method, code, duration, params, headers, result))
        }

        fun getLogs(): ArrayList<LogDao> {
            return logStore?.getLogs() ?: ArrayList()
        }

        fun getLog(idx: Int): LogDao? {
            return logStore?.getLog(idx)
        }

        fun setOnLogListener(listener: OnLogListener) {
            logStore?.setOnLogListener(listener)
        }

        fun clear() {
            logStore?.clear()
        }

        fun share(i: Int): String {
            return logStore?.share(i) ?: ""
        }
    }

    fun add(log: LogDao) {
        if (logList.size > 200) {
            logList.removeAt(0)
        }
        logList.add(log)
        Log.i("LogStore", "---> $log")
        if (logListener != null) {
            logListener?.onChange()
        }
    }

    fun getLog(idx: Int): LogDao? {
        if (logList.size > idx) {
           return logList[idx]
        }
        return null
    }

    fun getLogs(): ArrayList<LogDao> {
        return logList
    }

    fun setOnLogListener(listener: OnLogListener) {
        this.logListener = listener
    }

    fun clear() {
        logList.clear()
    }

    fun share(i: Int): String {
        var json = ""
        if (i > -1 && i < logList.size) {
            val log = logList[i]
            val gson = Gson()
            json = gson.toJson(log)
        }
        return json
    }

    interface OnLogListener {
        fun onChange()
    }
}