package com.ql.health.config

import android.os.Build

/**
 * Author: Meng
 * Date: 2023/08/03
 * Modify: 2023/08/03
 * Desc: 常量
 */
object Consts {
    var USER_ID = "" // 用户id
    var USER_NAME = "" // 用户名
    var USER_PHONE = "" // 用户手机号
    var USER_TOKEN = "" // 用户token
    var USER_JSON = "" // 用户信息json
//    var USER_AVATAR = "" // 用户头像

    var DEVICE_SN = "RL765901" // 设备sn
    var DEVICE_SDK = Build.VERSION.SDK_INT // 设备sdk号
    var VERSION = 10001L // 版本号
    var VERSION_NAME = "1.0.0" // 版本号
    var USER_AGENT = "sdk:0,device:p,version:10001,versionName:1.0.1"
    var DEVICE_NAME = "${Build.MANUFACTURER}_${Build.MODEL}" // 设备名

    fun getUserAgent(): String {
        return "sdk:$DEVICE_SDK,device:${Build.DEVICE},version:$VERSION,versionName:$VERSION_NAME,deviceName:$DEVICE_NAME"
    }

    fun getDeviceName(): String {
        // 如何获取 android 设备名


        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

    fun setUserAgent() {
        USER_AGENT = getUserAgent()
    }
}
