package com.dx.health.ui.act

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dx.health.R
import com.dx.health.custom.AppActivity
import com.dx.health.module.JSBridge

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class H5Activity : AppActivity() {
    private val TAG = "H5Activity"
    private lateinit var webView: WebView
    private lateinit var jsBridge: JSBridge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_h5)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        jsBridge.destroy()
    }

    private fun initView() {
        webView = findViewById<WebView>(R.id.h5_web)
        jsBridge = JSBridge(this, webView)

        // 启用JavaScript
        val settings = webView.getSettings()
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true
//        settings.allowFileAccessFromFileURLs = true
//        settings.allowUniversalAccessFromFileURLs = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // 注入JSBridge
        webView.addJavascriptInterface(jsBridge, "bridge")

        // 加载本地H5页面
        webView.loadUrl("http://192.168.253.100:8300/test")

        webView.setWebViewClient(object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.i(TAG, "onPageStarted: ${url}")
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Log.i(TAG, "onPageFinished: ${url}")
                // 调用JS方法
//                webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        //此处为 js 返回的结果
//                    }
//                });
            }

            @Deprecated("Deprecated in Java")
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Log.i(
                    TAG,
                    "onReceivedError: $errorCode $description $failingUrl"
                )
            }
        })
    }

    // 获取拍照结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "onActivityResult: $requestCode $resultCode")
    }
}