package com.ql.health.module.common.network

/**
 * Author: Meng
 * Date: 2023/06/28
 * Desc:
 */
class ResultData<T> (val msg: String, val code: Int,val data: T) {

    override fun toString(): String {
        return "ResData{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                '}'
    }
}

data class ResultData2<T, M> (val data: T, val meta: M, val page: Any) {
//    override fun toString(): String {
//        return "ResData{" +
//                "data=" + data +
//                ", meta='" + meta + '\'' +
//                ", page=" + page +
//                '}'
//    }
}
