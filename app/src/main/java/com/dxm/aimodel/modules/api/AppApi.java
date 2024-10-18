package com.dxm.aimodel.modules.api;


import com.dxm.aimodel.modules.network.ResultData;

import java.util.ArrayList;

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
    Call<Object> detail(@Query("id") int id);
}