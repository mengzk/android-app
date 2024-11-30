package com.dx.health.util

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import java.lang.reflect.Field

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
object PermissionUtils {
    private const val TAG = "PermissionUtil"
    val PERMISSION_SD_WRITE: Array<String> = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val PERMISSION_LOCATION: Array<String> = arrayOf<String>(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val PERMISSION_CAMERA: Array<String> = arrayOf<String>(Manifest.permission.CAMERA)
    val PERMISSION_RECORD: Array<String> = arrayOf<String>(Manifest.permission.RECORD_AUDIO)
    val PERMISSION_READ_PHONE: Array<String> = arrayOf<String>(Manifest.permission.READ_PHONE_STATE)
    val PERMISSION_AUDIO: Array<String> = arrayOf<String>(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val PERMISSION_CAMERAS: Array<String> = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    fun requestPermissions(context: Context?, permissions: Array<String>, request: Int): Int {
        val permissionList: MutableList<String> = ArrayList()
        for (i in permissions.indices) {
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    permissions[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.add(permissions[i])
            }
        }
        if (permissionList.size > 0) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                permissionList.toTypedArray<String>(),
                request
            )
            return permissionList.size
        }
        return 0
    }

    fun isHasSystemAlertWindow(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.SYSTEM_ALERT_WINDOW
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 文件权限读写
     *
     * @param context
     */
    fun requestSDCardWrite(context: Context?) {
        if (!isHasWritePermission(context)) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                PERMISSION_SD_WRITE,
                100
            )
        }
    }

    /**
     * 判断是否有文件读写的权限
     *
     * @param context
     * @return
     */
    fun isHasWritePermission(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * 请求地理位置
     *
     * @param context
     */
    fun requestLocationPermission(context: Context?) {
        if (!isHasLocationPermission(context)) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                PERMISSION_LOCATION,
                110
            )
        }
    }

    /**
     * 判断是否有地理位置
     *
     * @param context
     * @return
     */
    fun isHasLocationPermission(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * @param context
     * @return
     */
    fun isHasCameraPermission(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission(context: Context?) {
        if (!isHasCameraPermission(context)) {
            Log.d(TAG, "requestCameraPermission: ----->" + true)
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                PERMISSION_CAMERA,
                120
            )
        }
    }

    /**
     * 请求录音权限
     *
     * @param context
     */
    fun requestRecordPermission(context: Context?) {
        if (!isHasRecordPermission(context)) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                PERMISSION_RECORD,
                130
            )
        }
    }

    fun isHasRecordPermission(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * @param context
     * @return
     */
    fun isHasReadPhonePermission(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * @param context
     */
    fun requestReadPhonePermission(context: Context?) {
        if (!isHasReadPhonePermission(context)) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                PERMISSION_READ_PHONE,
                140
            )
        }
    }


    /**
     * 判断是否开启通知权限
     */
    fun isNotificationEnabled(context: Context): Boolean {
        try {
            val mAppOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

            val appOpsClass = Class.forName(AppOpsManager::class.java.name)

            val opPostNotificationValue: Field =
                appOpsClass.getDeclaredField("OP_POST_NOTIFICATION")
            val value = opPostNotificationValue.get(Int::class.java) as Int
            //app uid
            val uid = context.applicationInfo.uid
            //app 包名
            val pkg = context.applicationContext.packageName

            //检查权限
            val checkOpNoThrowMethod = appOpsClass.getMethod(
                "checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                String::class.java
            )
            return (checkOpNoThrowMethod.invoke(
                mAppOps,
                value,
                uid,
                pkg
            ) as Int == AppOpsManager.MODE_ALLOWED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
}