package com.dxm.robotchat.modules.network.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Gson gson;
    private TypeAdapter<T> typeAdapter;

    public GsonResponseBodyConverter(Gson gson, TypeAdapter<T> typeAdapter) {
        this.gson = gson;
        this.typeAdapter = typeAdapter;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        T result = null;
        try {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            result = typeAdapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        value.close();
        return result;
    }
}
