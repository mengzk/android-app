package com.ql.health.module.common.network.interceptor

import com.ql.health.module.common.Log3
import com.ql.health.module.common.exception.NetException
import com.ql.health.module.common.log.LogStore
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.nio.charset.Charset

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
 */
class LogInterceptor : Interceptor {
    private val tag = "LogInterceptor"
    private val utf8 = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = request.url

        var time3 = System.currentTimeMillis() // nanoTime()
        val headers = request.headers.toMultimap()
        val method = request.method
        val body3 = request.body
        var url = request.url.toString()
        if (url.contains("?")) {
            url = url.split("?")[0]
        }
        var params = httpUrl.query ?: ""
        if (body3 != null) {

            val contentType: MediaType? = body3.contentType()
            Log3.i(tag, "---> Response contentType $contentType")
            if (contentType != null) {
                if (!contentType.toString().contains("multipart/form-data")) {
                    val buffer = Buffer()
                    body3.writeTo(buffer)
                    val charset = contentType.charset(utf8)
                    params = charset?.let { buffer.readString(it) }.toString()
                }
            }
        }
        Log3.i(tag, "---> Request: $method, url:$url header:$headers, param:$params,")
        try {
            val response = chain.proceed(request)
            time3 = ((System.currentTimeMillis() - time3))
            var resultJson = ""

            val responseBody = response.body!!
            val contentLength = responseBody.contentLength()
            if (!response.promisesBody()) {
                Log3.i(tag, "---> Response END HTTP promises body")
            } else if (bodyHasUnknownEncoding(response.headers)) {
                Log3.i(tag, "---> Response END HTTP (encoded body omitted)")
            } else if (bodyIsStreaming(response)) {
                Log3.i(tag, "---> Response END HTTP (streaming)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE)
                var buffer = source.buffer
                if (headers.containsKey("gzip")) {
                    GzipSource(buffer.clone()).use { gzippedBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedBody)
                    }
                }
                if (contentLength != 0L) {
                    resultJson = buffer.clone().readString(utf8)
                }
                Log3.i(tag, "---> url:$url, status:${response.code}, time:$time3")
                Log3.i(tag, "---> Response: $resultJson")
            }
            LogStore.add(url, method, response.code, time3, params, headers, resultJson)

            return response
        } catch (e: Exception) {
            Log3.e(tag, "---> Response:$url, err: ${e.message}")
            LogStore.add(url, method, 403, time3, params, headers, e.message)
            throw NetException(e.message ?: "Err: $url failed connect ")
//            return chain.proceed(request)
        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun bodyIsStreaming(response: Response): Boolean {
        val contentType = response.body?.contentType()
        return contentType != null && contentType.type == "text" && contentType.subtype == "event-stream"
    }

}