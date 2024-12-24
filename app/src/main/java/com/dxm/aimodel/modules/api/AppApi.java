package com.dxm.aimodel.modules.api;


import com.dxm.aimodel.modules.network.ResultData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
 */
public interface AppApi {

    @GET("system/clients")
    Call<ResultData<ArrayList<String>>> getClients();

    @FormUrlEncoded
    @POST("customOrder/detail")
    Call<Object> detail(@Query("id") int id, @Header("token") String token);

    @Multipart
    @POST("file/upload")
    Call<ResultData<Object>> upload(@Part("tag") RequestBody tag, @Part MultipartBody.Part file);

    @Multipart
    @POST("file/uploads")
    Call<ResultData<Object>> uploads(@Part("tag") RequestBody tag, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("file/upload")
    Call<ResultData<Object>> uploadFileAndParam(@PartMap Map<String, RequestBody> map);
}