package com.dxm.aimodel.modules.bridge;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Author: Meng
 * Date: 2024/10/14
 * Modify: 2024/10/14
 * Desc:
 */

public class JSBridge {
    private static final String TAG = "Native";
    private  Context context;
    public JSBridge(Context context) {
        this.context = context;
    }

    /**
     * 调用原生方法
     */
    @JavascriptInterface
    public void call(String json) {

    }

    /**
     * 拨打电话
     * @param phone
     */
    @JavascriptInterface
    public void callPhone(String phone) {

    }

    /**
     * 拍照
     */
    @JavascriptInterface
    public void takePhoto() {

    }

    /**
     * 拍视频
     * @param path
     */
    @JavascriptInterface
    public void takeVideo(String path) {

    }
    /**
     * 选择媒体
     */
    @JavascriptInterface
    public void chooseMedia() {

    }

    /**
     * 选择文件
     */
    @JavascriptInterface
    public void chooseFile() {

    }

    /**
     * 提示Toast
     */
    @JavascriptInterface
    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 提示Loading
     */
    @JavascriptInterface
    public void showLoading(String msg) {

    }

    /**
     * 关闭Loading
     */
    @JavascriptInterface
    public void closeLoading() {

    }

    /**
     * 提示Dialog
     */
    @JavascriptInterface
    public void showDialog(String title, String msg) {

    }

    /**
     * 关闭页面
     */
    @JavascriptInterface
    public void close() {

    }

    /**
     * 打开新页面
     */
    @JavascriptInterface
    public void push(String url) {

    }

    /**
     * 返回上一页
     */
    @JavascriptInterface
    public void back() {

    }

    /**
     * 返回首页
     */
    @JavascriptInterface
    public void home() {

    }

    /**
     * 打开文档
     */
    @JavascriptInterface
    public void openDocs() {

    }

    /**
     * 扫码
     */
    @JavascriptInterface
    public void scan() {

    }

    /**
     * 获取位置
     */
    @JavascriptInterface
    public void getLocation() {

    }

    /**
     * 选择位置
     */
    @JavascriptInterface
    public void chooseLocation() {

    }

    /**
     * 获取定位
     */
    @JavascriptInterface
    public void getGeoLocation() {

    }

    /**
     * 打印
     */
    @JavascriptInterface
    public void print() {

    }

    /**
     * 保存数据
     */
    @JavascriptInterface
    public void saveData(String key, String value) {

    }

    /**
     * 读取数据
     */
    @JavascriptInterface
    public void readData(String key) {

    }

    /**
     * 删除数据
     */
    @JavascriptInterface
    public void removeData(String key) {

    }

    /**
     * 清空数据
     */
    @JavascriptInterface
    public void clearData() {

    }

    /**
     * 登录
     */
    @JavascriptInterface
    public void login() {

    }

    /**
     * 退出登录
     */
    @JavascriptInterface
    public void logout() {

    }

    /**
     * 获取用户信息
     */
    @JavascriptInterface
    public void getUserInfo() {

    }

    /**
     * 获取设备信息
     */
    @JavascriptInterface
    public void getDeviceInfo() {

    }

    /**
     * 获取网络状态
     */
    @JavascriptInterface
    public void getNetworkInfo() {

    }

    /**
     * 获取应用信息
     */
    @JavascriptInterface
    public void getAppInfo() {

    }

    /**
     * 获取系统信息
     */
    @JavascriptInterface
    public void getSystemInfo() {

    }

    /**
     * 获取传感器
     */
    @JavascriptInterface
    public void getSensorInfo() {

    }

    /**
     * 获取蓝牙
     */
    @JavascriptInterface
    public void getBluetoothInfo() {

    }

    /**
     * 获取WIFI
     */
    @JavascriptInterface
    public void getWifiInfo() {

    }

    /**
     * 获取NFC
     */
    @JavascriptInterface
    public void getNFCInfo() {

    }

}
