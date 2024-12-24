package com.ql.health.module.common.exception

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import kotlin.system.exitProcess

/**
 * Author: Meng
 * Date: 2023/06/16
 * Desc:
 */

class OneException private constructor(context: Context) : Thread.UncaughtExceptionHandler {
    // 系统默认的UncaughtException处理
    private val mDefaultHandler: Thread.UncaughtExceptionHandler?
    private val context: Context

    init {
        this.context = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        Log.e("AppException", ex.message!!)
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex)
        } else {
            val crashReport = getCrashReport(context, ex)
            writeLog(
                context.getExternalFilesDir("log")!!.absolutePath,
                SystemClock.currentThreadTimeMillis().toString() + ".log",
                crashReport
            )
            try {
                Thread.sleep(500)
            } catch (e: Exception) {
                Log.e("AppException", ex.message!!)
            }
            exitProcess(0)
        }
    }

    // 自定义异常处收集错误信息&发送错误报  true:处理该异常
    private fun handleException(ex: Throwable): Boolean {
        ex.printStackTrace()
        return true
    }

    // 获取APP崩溃异常报告
    private fun getCrashReport(context: Context, ex: Throwable): String {
        val exceptionStr = StringBuffer()
        val info: PackageInfo
        try {
            info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0L)
                )
            } else {
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            exceptionStr.append(
                """
                    Version: ${info.versionName}
                    """.trimIndent()
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                exceptionStr.append(
                    """
                    VersionCode: ${info.longVersionCode}
                    """.trimIndent()
                )
            } else {
                exceptionStr.append(
                    """
                    VersionCode: ${info.versionCode}
                    """.trimIndent()
                )
            }

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            exceptionStr.append("the application not found \n")
        }
        exceptionStr.append(
            """
                Android: ${Build.VERSION.RELEASE}(${Build.MODEL})
                """.trimIndent()
        )
        val stackTraceString = Log.getStackTraceString(ex)
        if (stackTraceString.isNotEmpty()) {
            exceptionStr.append(
                """
                    $stackTraceString
                    """.trimIndent()
            )
        } else {
            exceptionStr.append(
                """
                    Exception: ${ex.message}
                    """.trimIndent()
            )
            val elements = ex.stackTrace
            for (i in elements.indices) {
                exceptionStr.append(
                    """
                        ${elements[i]}
                        """.trimIndent()
                )
            }
        }
        return exceptionStr.toString()
    }

    private fun writeLog(filePath: String, fileName: String, content: String?): File? {
        var content2 = content
        if (content2 == null) content2 = ""
        val logFile = File(filePath + File.separator + fileName)
        try {
            val fos = FileOutputStream(logFile)
            fos.write(content2.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return logFile
    }

    private fun restartApp(ctx: Context) {
        ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)?.apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            ctx.startActivity(this)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(0)
        }
    }

    companion object {
        fun getAppExceptionHandler(context: Context): OneException {
            return OneException(context)
        }
    }
}
