package com.dx.health.module.common.network

/**
 * Author: Meng
 * Date: 2023/06/28
 * Desc:
 */
class ResultData<T> (val data: T?, val meta: Any?, val page: Any?) {

    override fun toString(): String {
        return "ResData{" +
                "data=" + data +
                ", meta='" + meta + '\'' +
                ", page=" + page +
                '}'
    }
}

class ResultData2<T, M> (val data: T?, val meta: M?, val page: Any?) {

    override fun toString(): String {
        return "ResData{" +
                "data=" + data +
                ", meta='" + meta + '\'' +
                ", page=" + page +
                '}'
    }
}

class ResultData3<T, M, P> (val data: T?, val meta: M?, val page: P?) {

    override fun toString(): String {
        return "ResData{" +
                "data=" + data +
                ", meta='" + meta + '\'' +
                ", page=" + page +
                '}'
    }
}

class ResultData4<T, P> (val data: T?, val meta: Any?, val page: P?) {

    override fun toString(): String {
        return "ResData{" +
                "data=" + data +
                ", meta='" + meta + '\'' +
                ", page=" + page +
                '}'
    }
}


class ResultData5<M> (val data: Any?, val meta: M?, val page: Any?) {

    override fun toString(): String {
        return "ResData{" +
                "data=" + data +
                ", meta='" + meta + '\'' +
                ", page=" + page +
                '}'
    }
}

class ResultData6<P> (val data: Any?, val meta: Any?, val page: P?) {

    override fun toString(): String {
        return "ResData{" +
                "data=" + data +
                ", meta='" + meta + '\'' +
                ", page=" + page +
                '}'
    }
}