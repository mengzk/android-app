package com.dxm.robotchat.modules.network.retrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public class MyGsonConverterFactory extends Converter.Factory {
    private Gson gson;

    public MyGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    public static MyGsonConverterFactory create() {
        Gson gson = new Gson();
        return new MyGsonConverterFactory(gson);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
//        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
        TypeAdapter adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter(gson, adapter);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//        return super.responseBodyConverter(type, annotations, retrofit);
        TypeAdapter adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter(gson, adapter);
    }
}
