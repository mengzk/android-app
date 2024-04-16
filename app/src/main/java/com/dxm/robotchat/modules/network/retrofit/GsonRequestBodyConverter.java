package com.dxm.robotchat.modules.network.retrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Author: Meng
 * Date: 2023/04/13
 * Desc:
 */
public class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private Gson gson;
    private TypeAdapter<T> typeAdapter;
    private MediaType MEDIA_TYPE  = MediaType.parse("application/json; charset=UTF-8");
    private Charset UTF_8 = Charset.forName("UTF-8");

    public GsonRequestBodyConverter(Gson gson, TypeAdapter<T> typeAdapter) {
        this.gson = gson;
        this.typeAdapter = typeAdapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        OutputStreamWriter writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        typeAdapter.write(jsonWriter, value);
        jsonWriter.close();
        return RequestBody.create(buffer.readByteString(), MEDIA_TYPE);
    }
}
