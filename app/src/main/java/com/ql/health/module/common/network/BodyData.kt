package com.ql.health.module.common.network

/**
 * Author: Meng
 * Date: 2023/06/16
 * Desc:
 */
data class BodyData<T>(val code: Int, val message: String, val data: T?) {
    override fun toString(): String {
        return "---> {" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }
}

data class DataList<T>(val total: Int, val pageSize: Int, val data: T) {}