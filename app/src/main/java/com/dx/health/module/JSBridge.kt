package com.dx.health.module

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import org.json.JSONObject


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class JSBridge(private var context: Activity, private var webView: WebView) {

    fun destroy() {
        webView.removeJavascriptInterface("bridge")
        webView.destroy()
    }

    /**
     * 调用原生方法
     */
    @JavascriptInterface
    fun test(json: String) {
        Log.i(TAG, "test: $json")
    }

    /**
     * 调用原生异步方法
     * <body>
     * <button onclick="callAndroid()">Call Android</button>
     *
     * <script type="text/javascript">
    bridge.asyncTest("1234567890", "onAsyncRes");
    window.onAsyncRes = (msg) => {
    console.log('onAsyncRes', msg);
    }
    或者
    function onAsyncRes() {
    console.log('onAsyncRes', msg);
    }
    </script>
    </body> *
     */
    @JavascriptInterface
    fun asyncTest(json: String, callback: String) {
        Log.i(TAG, "test: $json")
        Log.i(TAG, "callWithResult: $callback")
        Thread {
            try {
                Thread.sleep(2000)
                val result = "Result from Android"
                // Run on UI thread to call JavaScript
                (context as Activity).runOnUiThread {
                    webView.evaluateJavascript(
                        "javascript:$callback('$result')",
                        null
                    )
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }

    /**
     * 调用原生方法
     */
    @JavascriptInterface
    fun call(json: String) {
        Log.i(TAG, "call: $json")
    }

    /**
     * 拨打电话
     * @param phone
     */
    @JavascriptInterface
    fun callPhone(phone: String?) {
    }

    /**
     * 拍照
     */
    @JavascriptInterface
    fun takePhoto() {
    }

    /**
     * 拍视频
     * @param path
     */
    @JavascriptInterface
    fun takeVideo(path: String?) {
    }

    /**
     * 选择媒体
     */
    @JavascriptInterface
    fun chooseMedia() {
    }

    /**
     * 选择文件
     */
    @JavascriptInterface
    fun chooseFile() {
    }

    /**
     * 提示Toast
     */
    @JavascriptInterface
    fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 提示Loading
     */
    @JavascriptInterface
    fun showLoading(msg: String?) {
    }

    /**
     * 关闭Loading
     */
    @JavascriptInterface
    fun closeLoading() {
    }

    /**
     * 提示Dialog
     */
    @JavascriptInterface
    fun showDialog(title: String?, msg: String?) {
    }

    /**
     * 关闭页面
     */
    @JavascriptInterface
    fun close() {
    }

    /**
     * 打开新页面
     */
    @JavascriptInterface
    fun push(url: String?) {
    }

    /**
     * 返回上一页
     */
    @JavascriptInterface
    fun back() {
    }

    /**
     * 返回首页
     */
    @JavascriptInterface
    fun home() {
    }

    /**
     * 打开文档
     */
    @JavascriptInterface
    fun openDocs() {
    }

    /**
     * 扫码
     */
    @JavascriptInterface
    fun scan() {
    }

    /**
     * 获取位置
     */
    @JavascriptInterface
    fun getLocation() {
    }

    /**
     * 选择位置
     */
    @JavascriptInterface
    fun chooseLocation() {
    }

    /**
     * 获取定位
     */
    @JavascriptInterface
    fun getGeoLocation() {
    }

    /**
     * 打印
     */
    @JavascriptInterface
    fun print() {
    }

    /**
     * 保存数据
     */
    @JavascriptInterface
    fun saveData(key: String?, value: String?) {
    }

    /**
     * 读取数据
     */
    @JavascriptInterface
    fun readData(key: String?) {
    }

    /**
     * 删除数据
     */
    @JavascriptInterface
    fun removeData(key: String?) {
    }

    /**
     * 清空数据
     */
    @JavascriptInterface
    fun clearData() {
    }

    /**
     * 登录
     */
    @JavascriptInterface
    fun login() {
    }

    /**
     * 退出登录
     */
    @JavascriptInterface
    fun logout() {
    }

    /**
     * 获取用户信息
     */
    @JavascriptInterface
    fun getUserInfo() {
    }

    /**
     * 获取设备信息
     */
    @JavascriptInterface
    fun getDeviceInfo() {
    }

    @JavascriptInterface
    public fun getScreen(): String {
        val metric = DisplayMetrics()
        context.windowManager.defaultDisplay.getRealMetrics(metric)
        val width = metric.widthPixels // 宽度（PX）
        val height = metric.heightPixels // 高度（PX）
        val density = metric.density // 密度（0.75 / 1.0 / 1.5）
        val densityDpi = metric.densityDpi
        val json = JSONObject()
        try {
            json.put("width", width)
            json.put("height", height)
            json.put("density", density)
            json.put("densityDpi", densityDpi)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return json.toString()
    }

    /**
     * 获取网络状态
     */
    @JavascriptInterface
    fun getNetworkInfo() {
    }

    /**
     * 获取应用信息
     */
    @JavascriptInterface
    fun getAppInfo() {
    }

    /**
     * 获取系统信息
     */
    @JavascriptInterface
    fun getSystemInfo() {
    }

    /**
     * 获取传感器
     */
    @JavascriptInterface
    fun getSensorInfo() {
    }

    /**
     * 获取蓝牙
     */
    @JavascriptInterface
    fun getBluetoothInfo() {
    }

    /**
     * 获取WIFI
     */
    @JavascriptInterface
    fun getWifiInfo() {
    }

    /**
     * 获取NFC
     */
    @JavascriptInterface
    fun getNFCInfo() {
    }

    companion object {
        private const val TAG = "Native"
    }
}
