package com.dxm.aimodel.modules.network.lnterceptor;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public class LogInterceptor implements Interceptor {
    private String TAG = "LogInterceptor";
    private Charset utf8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        HttpUrl httpUrl = request.url();

        Map headers = request.headers().toMultimap();
        String method = request.method();
        RequestBody paramsBody = request.body();

        String url = httpUrl.toString();
        String params = httpUrl.query();
//        if (url.contains("?")) {
//            url = url.split("?")[0];
//        }
        if (paramsBody != null) {
            Buffer buffer = new Buffer();
            paramsBody.writeTo(buffer);
            Charset charset = utf8;
            MediaType contentType = paramsBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(utf8);
            }
            params = buffer.readString(charset);
        }

        Log.i(TAG, String.format("-----> Request: %s, %s, %s, %s", method, url, params, headers));

        try {
            Response response = chain.proceed(request);
            String resultJson = "";
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
//            if (response.promisesBody()) {
//                Log.i(TAG, "---> END HTTP promises body");
//            } else
            if (bodyHasUnknownEncoding(response.headers())) {
                Log.i(TAG, "---> END HTTP (encoded body omitted)");
            } else if (bodyIsStreaming(response)) {
                Log.i(TAG, "---> END HTTP (streaming)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();
                if (headers.containsKey("gzip")) {
                    GzipSource gs = new GzipSource(buffer.clone());
                    buffer = new Buffer();
                    buffer.writeAll(gs);
                }
                if (contentLength != 0L) {
                    resultJson = buffer.clone().readString(utf8);
                }
                Log.i(TAG, "-----> Response: " + resultJson);
            }
            return response;
        } catch (Exception e) {
//            Log.e(TAG, String.format("-----> response: %s, %s", url, e.getMessage()));
//            String msg = e.getMessage() != null ? e.getMessage() : "Err: $url failed connect ";
//            throw new IOException(msg);
            return chain.proceed(request);
        }
    }


    private boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        if (contentEncoding == null) {
            return false;
        }
        return !contentEncoding.equals("identity") && !contentEncoding.equals("gzip");
    }

    private boolean bodyIsStreaming(Response response) {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            MediaType contentType = responseBody.contentType();
            return contentType != null && contentType.type().equals("text") && contentType.subtype().equals("event-stream");
        }
        return true;
    }

}
