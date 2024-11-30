package com.dx.health.module.common.network

/**
 * Author: Meng
 * Date: 2023/06/16
 * Desc:
 */
class BodyData<T>(val code: Int, val msg: String?, val result: T?) {

    override fun toString(): String {
        return "---> ResultData: {" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}'
    }
}