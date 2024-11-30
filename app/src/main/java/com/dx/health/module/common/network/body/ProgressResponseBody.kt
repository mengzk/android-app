package com.dx.health.module.common.network.body

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okio.*

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
    val client = new OkHttpClient.Builder()
    .addNetworkInterceptor(chain -> {
        val response = chain.proceed(chain.request())
        return response.newBuilder()
            .body(ProgressResponseBody(response.body, object : ProgressResponseBody.ProgressListener {
                override fun progress(readSize: Long, totalLength: Long, done: Boolean) {
                }
            }))
            .build()
    })
    .build();
 */
class ProgressResponseBody(
    val responseBody: ResponseBody?,
    val progressListener: OnProgressListener
) :
    ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long {
        return responseBody?.contentLength() ?: 0
    }

    override fun contentType(): MediaType? {
        return responseBody?.contentType() ?: "application/json; charset=UTF-8".toMediaType()
    }

    override fun source(): BufferedSource {
        if (responseBody == null) {
            bufferedSource = source().buffer()
        } else {
            if (bufferedSource == null) {
                bufferedSource = source(responseBody.source()).buffer()
            }
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var readSize = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = sink.let { super.read(it, byteCount) }
                readSize += if (bytesRead != -1L) bytesRead else 0
                val length = responseBody?.contentLength() ?: 0
                val end = bytesRead == -1L
                progressListener.progress(readSize, length, end)
                return bytesRead
            }
        }
    }
}