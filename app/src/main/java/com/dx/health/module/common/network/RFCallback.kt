package com.dx.health.module.common.network

import com.dx.health.module.common.Log3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Author: Meng
 * Date: 2023/06/28
 * Desc:
 */
abstract class RFCallback<T> : Callback<T> {

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFail(1001, t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val data = response.body()
        Log3.i("RFCallback", "---> onResponse: $data")
        if (data != null) {
            try {
//                if (data.code == 0 && data.result != null) {
//                    onResult(data.result)
//                } else {
//                    val exc = when (data.code) {
//                        404 -> Exception("请求地址不存在")
//                        405 -> Exception("请求方式错误，请联系开发人员")
//                        500 -> Exception(data.msg)
//                        502 -> Exception("服务重启中, 请稍后")
//                        504 -> Exception("网关连接超时, 请稍后")
//                        else -> Exception("服务未知错误，请稍后")
//                    }
//                    onFail(data.code, exc)
//                }
            } catch (e: Exception) {
                onFail(1002, e)
            }
        } else {
            onFail(1003, Exception("接口返回数据格式异常"))
        }
    }

    open fun onFail(code: Int, e: Throwable) {
        Log3.e("OKCallback", "---> Fail: code=$code, msg=${e.message}")
    }

    open fun onResult(result: T) {}
}