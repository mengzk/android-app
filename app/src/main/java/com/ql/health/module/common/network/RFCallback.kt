package com.ql.health.module.common.network

import com.ql.health.module.common.Log3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Author: Meng
 * Date: 2023/06/28
 * Desc:
 */
abstract class RFCallback<T> : Callback<BodyData<T>> {

    override fun onFailure(call: Call<BodyData<T>>, t: Throwable) {
//        Log3.e("RFCallback", "---> fail: ${t.message}")
        onFail(10010, t)
    }

    override fun onResponse(call: Call<BodyData<T>>, response: Response<BodyData<T>>) {
        val data = response.body()

        if (data != null) {
            try {
                if (data.code == 0 && data.data != null) {
                    onResult(data.data)
                } else {
                    val exc = when (data.code) {
                        404 -> Exception("请求地址不存在")
                        405 -> Exception("请求方式错误，请联系开发人员")
                        500 -> Exception(data.message)
                        502 -> Exception("服务重启中, 请稍后")
                        504 -> Exception("网关连接超时, 请稍后")
                        else -> Exception("服务未知错误，请稍后")
                    }
                    onFail(data.code, exc)
                }
            } catch (e: Exception) {
                Log3.i("RFCallback", "---> response catch: $data")
                onFail(10020, e)
            }
        } else {
            Log3.i("RFCallback", "---> response data: $data")
            onFail(10030, Exception("接口返回数据格式异常"))
        }
    }

    open fun onFail(code: Int, e: Throwable) {
        Log3.e("RFCallback", "---> fail code:$code ${e.message}")
    }

    abstract fun onResult(res: T)
}