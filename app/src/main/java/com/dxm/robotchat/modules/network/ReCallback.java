package com.dxm.robotchat.modules.network;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: Meng
 * Date: 2023/04/26
 * Desc:
 */
public abstract class ReCallback<T> implements Callback<T> {
    private String TAG = "NetCallback";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (response.isSuccessful()) {
                onResult(response.body());
            } else {
                onFail(response.code(), response.message());
            }
        } catch (Exception e) {
            Log.i(TAG, String.format("onResponse -----> %s", e.getMessage()));
            onFail(response.code(), response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.i(TAG, String.format("onFailure -----> %s", t.getMessage()));
        Log.i(TAG, String.format("Request -----> %s", call.request()));
        onFail(10101, t.getMessage());
    }

    protected void onFail(int code, String msg) {
        Log.i(TAG, String.format("onFail -----> code:%d, msg:%s", code, msg));
    }

    protected abstract void onResult(T data);
}
