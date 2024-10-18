package com.dxm.aimodel.modules.network;

/**
 * Author: Meng
 * Date: 2023/04/22
 * Desc:
 */
public class ResultChat<T> {
    public int code;
    public boolean success;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "ResultData{" +
                "code=" + code +
                "success=" + success +
                ", message='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
