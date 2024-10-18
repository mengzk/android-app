package com.dxm.aimodel.modules.network.lnterceptor;

import androidx.annotation.NonNull;
import com.dxm.aimodel.modules.network.Config;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public class NetInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request result = chain.request();

        Request.Builder builder = result.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
//                .addHeader("Authorization", "baaa696e5a564afe5e4ce49236e852b0.dV3gTIO7SasX8K6H")
                .addHeader("Authorization", Config.token)
                .addHeader("userAgent", "version:24, device:Android");

//        if(result.method.lowercase() == "get") {
//            val newUrl = result.url.newBuilder().addQueryParameter("TOKEN", token).build()
//            builder.url(newUrl)
//        }else {}
        return chain.proceed(builder.build());
    }
}