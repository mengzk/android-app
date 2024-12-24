package com.ql.health.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class UpdateService : JobService() {
    private val Tag = "UpdateService"
//    private val JOB_ID = 1000
    private val apkUrl =
        "https://bnq-app-store.oss-cn-shanghai.aliyuncs.com/app_package/test/apk/shadowsocks/v1680750650870%26shadowsocks.apk" // 下载包安装路径
    private var savePath = ""
    private val apkName = "app_pack.apk"
    private var mContext: Context? = null
    private var downing = false
    private val diskIO: Executor = Executors.newSingleThreadExecutor()

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        mContext = applicationContext
        savePath = mContext?.getFilesDir().toString() + "/apk"
        downApk()
        return false
    }

    override fun onStopJob(jobParameters: JobParameters?): Boolean {
        return false
    }

    private fun downApk() {
        diskIO.execute { handleWork() }
    }

    private fun handleWork() {
        if (downing) {
            return
        }
        downing = true
        Log.i(Tag, "=============> 开始下载")
        val fos: FileOutputStream
        val ism: InputStream
        try {
            val url = URL(apkUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 120000
            conn.connect()
            ism = conn.inputStream
            var apkFile = File(savePath)
            if (!apkFile.exists()) {
                apkFile.mkdirs()
            }
            apkFile = File(savePath, apkName)

            Log.i(Tag, "=============> APK Path $savePath")

            fos = FileOutputStream(apkFile)
            val total = conn.contentLength.toLong()
            var length = 0
            val buf = ByteArray(16384)
            var num = 0
            while ((ism.read(buf).also { num = it }) > 0) {
                length += num
                val progress = (length * 100 / total).toInt()
                Log.i(Tag, "=============> $progress")
                fos.write(buf, 0, num)
            }
            fos.flush()
            fos.close()
            ism.close()
            installApk()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        downing = false
    }

    private fun installApk() {
        val apkFile = File(savePath, apkName)
        if (!apkFile.exists()) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val isGranted = packageManager.canRequestPackageInstalls()
            if (!isGranted) {
                Log.i(Tag, "=============> 请开启安装Apk权限")
                return
            }
        }
        Log.i(Tag, "=============> apkFile: " + apkFile.path)
        val intent = Intent(Intent.ACTION_VIEW)
        val uri: Uri
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                // 7.0  / com.xxx.xxx.fileprovider为上述manifest中provider所配置相同
                uri = FileProvider.getUriForFile(
                    mContext!!,
                    mContext!!.packageName + ".file_provider",
                    apkFile
                )

                intent.setAction(Intent.ACTION_INSTALL_PACKAGE)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // 7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，该过程没有用户交互
            } else { // 7.0以下
                uri = Uri.fromFile(apkFile)
                intent.setAction(Intent.ACTION_VIEW)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            Log.i(Tag, "=============> url:" + uri.path)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            mContext!!.startActivity(intent)
            Log.i(Tag, "=============> 安装中...")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        var JOB_CODE: Int = 1231
        fun start(context: Context) {
            val serviceName = ComponentName(context, UpdateService::class.java)
            val jobBuilder = JobInfo.Builder(JOB_CODE, serviceName)
            jobBuilder.setPeriodic((1000 * 60 * 15).toLong())
            val myJob = jobBuilder.build()

            val scheduler = context.getSystemService(JobScheduler::class.java)
            scheduler.schedule(myJob)
        }
    }
}