package com.dx.health.module.common.network.interceptor

import com.dx.health.module.common.network.body.OnProgressListener
import com.dx.health.module.common.network.body.ProgressResponseBody
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
 *   val cache = Cache(cacheDir, 1024 * 1024)
 *   cache.evictAll()
 *   new OkHttpClient.Builder()
.cache(cache)
.build();
 */
class ProgressInterceptor(val listener: OnProgressListener) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        return response.newBuilder()
            .body(ProgressResponseBody(response.body, listener))
            .build()
    }
}