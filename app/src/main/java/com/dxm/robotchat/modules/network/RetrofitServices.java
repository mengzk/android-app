package com.dxm.robotchat.modules.network;

import com.dxm.robotchat.config.AppConfig;
import com.dxm.robotchat.modules.api.AppApi;
import com.dxm.robotchat.modules.api.ChatApi;
import com.dxm.robotchat.modules.network.retrofit.MyGsonConverterFactory;
import com.dxm.robotchat.utils.JWTUtils;

import retrofit2.Retrofit;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public class RetrofitServices {
    private static RetrofitServices instance;

    private AppApi appApi;
    private ChatApi chatApi;
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .client(Network.client())
            .addConverterFactory(MyGsonConverterFactory.create());

    private RetrofitServices() {
        setAppApi();
        setChatApi();

        Config.token = JWTUtils.getJWTToken(AppConfig.apiKey);
    }

    public static synchronized RetrofitServices getInstance() {
        if (instance == null) {
            instance = new RetrofitServices();
        }
        return instance;
    }

    private void setAppApi() {
        String url = Config.getTagHost("def");
        Retrofit retrofit = builder.baseUrl(url)
                .build();
        appApi = retrofit.create(AppApi.class);
    }

    private void setChatApi() {
        String url = Config.getTagHost("chat");
        Retrofit retrofit = builder.baseUrl(url)
                .build();
        chatApi = retrofit.create(ChatApi.class);
    }

    public AppApi getAppApi() {
        return appApi;
    }

    public ChatApi getChatApi() {
        return chatApi;
    }
}
