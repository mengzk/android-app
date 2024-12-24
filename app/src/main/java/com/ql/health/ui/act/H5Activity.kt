package com.ql.health.ui.act

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.RenderProcessGoneDetail
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebViewRenderProcessClient
import com.ql.health.R
import com.ql.health.config.Consts
import com.ql.health.custom.AppActivity
import com.ql.health.module.JSBridge

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
        webView.clearCache(true)
        super.onDestroy()
        jsBridge.destroy()
    }

    @SuppressLint("SetJavaScriptEnabled")
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
//        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // 注入JSBridge
        webView.addJavascriptInterface(jsBridge, "bridge")

        val pulseId = intent.getStringExtra("pulseId")
        val tongueId = intent.getStringExtra("tongueId")
        Log.i(TAG, "-------> pulseId=$pulseId&tongueId=$tongueId")
        // 加载本地H5页面
        webView.loadUrl("http://192.168.253.154:8073#/?pulseId=$pulseId&tongueId=$tongueId")

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
                val user = Consts.USER_JSON
                // 调用JS方法
                webView.evaluateJavascript("javascript:setAppAgent('$user')", ValueCallback<String> { value ->
                        Log.i(TAG, "evaluateJavascript: $value")
                    })
            }

            @Deprecated("Deprecated in Java")
            override fun onReceivedError(
                view: WebView,
                errCode: Int,
                desc: String,
                failUrl: String
            ) {
                Log.i(TAG, "onReceivedError: $errCode $desc $failUrl")
            }
        })
    }
}