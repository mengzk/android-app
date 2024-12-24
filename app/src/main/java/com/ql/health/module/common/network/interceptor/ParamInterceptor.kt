package com.ql.health.module.common.network.interceptor

import android.os.Build
import android.util.Log
import com.ql.health.config.Consts
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
class ParamInterceptor : Interceptor {
    val version = Build.VERSION.SDK_INT
    val device = Build.DEVICE

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val result = chain.request()

        val builder = result.newBuilder()
//            .addHeader("Cache-Control", "max-age=300")
//            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .addHeader("userAgent", "version:$version,device:$device")
            .addHeader("Authorization", "Bearer ${Consts.USER_TOKEN}")
//            .addHeader("Token", Consts.USER_TOKEN)

//        if(result.method.lowercase() == "get") {
//            val newUrl = result.url.newBuilder().addQueryParameter("TOKEN", token).build()
//            builder.url(newUrl)
//        }else {}
        return chain.proceed(builder.build())
    }
}