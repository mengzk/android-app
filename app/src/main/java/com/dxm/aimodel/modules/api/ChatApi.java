package com.dxm.aimodel.modules.api;

import com.dxm.aimodel.modules.model.body.Model4Body;
import com.dxm.aimodel.modules.model.body.PromptBody;
import com.dxm.aimodel.modules.model.entity.ChatEntity;
import com.dxm.aimodel.modules.network.ResultChat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
 */
public interface ChatApi {

//    @FormUrlEncoded
    @GET("completions")
    Call<ResultChat<Object>> chatModelV3Api(@Query("model") String model, @Query("message") String message);

    @POST("paas/v4/chat/completions")
    Call<ChatEntity> chatModelV4Api(@Body Model4Body body);

    @POST("llm-application/open/model-api/{id}/invoke")
    Call<ChatEntity> chatModelV3Api(@Path("id") String id, @Body PromptBody body);
}