package com.dxm.aimodel.modules.api;


import com.dxm.aimodel.modules.network.ResultData;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
 */
public interface HealthApi {

    @GET("system/clients")
    Call<ResultData<ArrayList<String>>> getClients();

    @FormUrlEncoded
    @POST("customOrder/detail")
    Call<Object> detail(@Query("id") int id);

    @POST("file/upload")
    Call<ResultData<ArrayList<String>>> upload(@Body File file);
}