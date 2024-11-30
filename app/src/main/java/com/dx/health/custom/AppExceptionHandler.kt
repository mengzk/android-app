package com.dx.health.custom

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.os.Looper
import android.os.Process
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.exitProcess

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class AppExceptionHandler(private var context: Context): Thread.UncaughtExceptionHandler {
    private val TAG = "AppExceptionHandler"

    // 系统默认的UncaughtException处理类
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    fun init() {
        //获取系统默认的UncaughtException处理器
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该MyCrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!handleExample(e) && defaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理 目的是判断异常是否已经被处理
            defaultHandler!!.uncaughtException(t, e)
        } else {
            try { //Sleep 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
                Thread.sleep(3000)
            } catch (e1: InterruptedException) {
                e1.printStackTrace()
                Log.d(TAG, "uncaughtException: " + e1.message)
            } catch (e2: Exception) {
                e2.printStackTrace()
                Log.d(TAG, "uncaughtException: " + e2.message)
            }
            /** 关闭App 与下面的restartApp重启App保留一个就行 看你需求  */
            // 如果不关闭程序,会导致程序无法启动,需要完全结束进程才能重新启动
            Process.killProcess(Process.myPid())
            exitProcess(1)
            //            restartApp();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 将异常信息保存 发送错误报告等操作均在此完成.
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleExample(ex: Throwable?): Boolean {
        // 如果已经处理过这个Exception,则让系统处理器进行后续关闭处理
        if (ex == null) {
            return false
        }
        Thread {
            // Toast 显示需要出现在一个线程的消息队列中
            Looper.prepare()
            Toast.makeText(context, "抱歉，程序出现异常!", Toast.LENGTH_SHORT).show()
            Looper.loop()
        }.start()
        saveCrashInfo(ex)
        return true
    }

    /**
     * 重启应用
     */
    fun restartApp() {
//        Intent intent = new Intent(AppApplication.getContext(), SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(1);

        // 重启应用

        context.startActivity(context.packageManager.getLaunchIntentForPackage(context.packageName))
        //干掉当前的程序
        Process.killProcess(Process.myPid())
    }

    /**
     * 保存错误信息到文件中
     */
    private fun saveCrashInfo(ex: Throwable) {
        val writer: Writer = StringWriter()
        val printWriter: PrintWriter = PrintWriter(writer)
        try {
            ex.printStackTrace(printWriter)
            var exCause = ex.cause
            while (exCause != null) {
                exCause.printStackTrace(printWriter)
                exCause = exCause.cause
            }
            printWriter.close()

            var path = ""
            // 判断存储是否正常使用
//            path = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
//                Environment.getExternalStorageDirectory().path + "/CrashLog"
//            } else {
//                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + "/CrashLog"
//            }
            writeFile(writer, path)
        } catch (exception: Exception) {
            printWriter.close()
        }
    }

    private fun writeFile(writer: Writer, path: String) {
        try {
            val fileName = "Log_" + stampDate() + ".txt"
            //  File file = new File(path);
            val file =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            if (!file.exists()) {
                file.mkdirs()
            }
            Log.i(TAG, "writeFile: " + file.path + "/" + fileName)
            val fos = FileOutputStream(File(path, fileName))
            try {
                fos.write(writer.toString().toByteArray())
                fos.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            fos.close()
            Log.i(TAG, "writeFile: $path/$fileName")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     */
    @SuppressLint("SimpleDateFormat")
    private fun stampDate(): String {
        val nowTime: Date = Date(System.currentTimeMillis())
        val sdFormatter: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmddss")
        return sdFormatter.format(nowTime)
    }
}
