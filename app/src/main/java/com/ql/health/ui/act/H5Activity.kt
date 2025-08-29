package com.ql.health.ui.act

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.ql.health.R
import com.ql.health.config.Configs
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
    private lateinit var progressBar: ProgressBar
    private lateinit var jsBridge: JSBridge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_h5)

        initView()
        setDuration(360000L)
    }

    override fun onDestroy() {
        webView.clearCache(true)
        super.onDestroy()
        jsBridge.destroy()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        progressBar = findViewById(R.id.h5_progress)
        webView = findViewById<WebView>(R.id.h5_web)
        jsBridge = JSBridge(this, webView)

        // 启用JavaScript
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // Set user agent to match a standard browser
        settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"


        // 注入JSBridge
        webView.addJavascriptInterface(jsBridge, "ql")

        val pulseId = intent.getStringExtra("pulse")
        val tongueId = intent.getStringExtra("tongue")
        Log.i(TAG, "---> pulseId=$pulseId&tongueId=$tongueId")
        // 加载本地H5页面
        webView.loadUrl("${Configs.h5Url}?rid=${pulseId}&sid=${tongueId}")
//        webView.loadUrl("http://192.168.31.82:8930?rid=cv8oqeujq8hn9n6cu4lg&sid=")

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
//                val cookieManager = CookieManager.getInstance()
//                val cookies = cookieManager.getCookie(url)
//                Log.i("H5Activity", "Cookies for $url: $cookies")
                progressBar.visibility = ProgressBar.GONE
                webView.visibility = WebView.VISIBLE
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

        // 进度条
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }

        // 下载监听
        webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType))

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }

    }
}