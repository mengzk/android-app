package com.dx.health.module.common.exception

import android.accounts.NetworkErrorException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

/**
 * Author: Meng
 * Date: 2023/08/03
 * Modify: 2023/08/03
 * Desc:
 */
object ExceptionHelper {

    /**
     * 处理异常
     */
    fun parseException(e: Throwable, context: CoroutineContext) {
        e.printStackTrace()
        when (e) {
            is HttpException -> {
//                Logger.e(e.code())
            }

            is SocketTimeoutException -> {
            }

            is UnknownHostException, is NetworkErrorException -> {

            }

            is MalformedJsonException, is JsonSyntaxException -> {

            }

            is InterruptedIOException -> {

            }

            is ConnectException -> {}

            is IOException -> {

            }

            else -> {

            }
        }
    }
}