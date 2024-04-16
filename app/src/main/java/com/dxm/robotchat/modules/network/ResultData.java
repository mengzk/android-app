package com.dxm.robotchat.modules.network;

/**
 * Author: Meng
 * Date: 2023/04/22
 * Desc:
 */
public class ResultData<T> {
    public int code;
    public String message;
    public T data;

    @Override
    public String toString() {
        return "ResultData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
