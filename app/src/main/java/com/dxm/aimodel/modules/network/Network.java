package com.dxm.aimodel.modules.network;


import com.dxm.aimodel.modules.network.lnterceptor.LogInterceptor;
import com.dxm.aimodel.modules.network.lnterceptor.NetInterceptor;
import com.dxm.aimodel.modules.network.ssl.SSLSocketFactoryCompat;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 * https://github.com/square/okhttp/tree/master/samples/guide/src/main/java/okhttp3/recipes
 */
public class Network {
    private static MediaType MEDIA_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static MediaType MEDIA_PNG = MediaType.parse("image/png");
    private static MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient okClient;

    public static OkHttpClient client() {
        if (okClient == null) {
            create();
        }
        return okClient;
    }

    private static void create() {
        okClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new NetInterceptor())
                .addInterceptor(new LogInterceptor())
                .build();
    }

    /**
     * OkHttp在4.4及以下不支持TLS协议的解决方法
     * javax.net.ssl.SSLHandshakeException: javax.net.ssl.SSLProtocolException
     * @param okhttpBuilder
     */
    private synchronized static OkHttpClient.Builder setOkHttpSsl(OkHttpClient.Builder okhttpBuilder) {
        try {
            // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
            final X509TrustManager trustAllCert =
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    };
            final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
            okhttpBuilder.sslSocketFactory(sslSocketFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return okhttpBuilder;
    }
}
