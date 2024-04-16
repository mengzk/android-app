package com.dxm.robotchat.modules.network.body;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
 * OkHttpClient client = new OkHttpClient.Builder()
 * .addNetworkInterceptor(chain -> {
 * Response originalResponse = chain.proceed(chain.request());
 * return originalResponse.newBuilder()
 * .body(new ProgressResponseBody(originalResponse.body(), progressListener))
 * .build();
 * })
 * .build();
 */
public class ProgressResponseBody extends ResponseBody {

    private BufferedSource bufferedSource;
    private ResponseBody responseBody;
    private OnProgressListener progressListener;

    public ProgressResponseBody(ResponseBody responseBody, OnProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @NonNull
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }

    public interface OnProgressListener {
        void update(long curLength, long contentLength, boolean done);
    }
}
