package com.dx.health.module.api

import com.dx.health.module.common.network.BodyData
import com.dx.health.module.common.network.ResultData
import okhttp3.MultipartBody
import java.io.File;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 *  AppApis.mainApi.checkVersion("").enqueue(object : OKCallback<ResultData<Any>>() {
 *     override fun onResult(result: ResultData<Any>) {
 *          super.onResult(result)
 *     }
 *     override fun onFail(code: Int, e: Throwable) {
 *         super.onFail(code, e)
 *     }
 *  })
 */
interface MainApi {
    //    @Headers(*["Token3: 131231","Token: 131231"])
    //    @GET("customOrder/detail")
    //    fun detail(@Query("id") id: Int): Call<ResultData<Any>>

    @Multipart
    @POST("healthcare/api/upload/upload")
    fun upload(@Part file: MultipartBody.Part, @Part("userId") tag: RequestBody?): Call<ResultData<Any>>

    @Multipart
    @POST("file/upload")
    fun upload3(@Part file: MultipartBody.Part, @Part("userId") tag: RequestBody?): Call<ResultData<Any>>

    @Multipart
    @POST("file/uploads")
    fun uploads3(@Part files: List<MultipartBody.Part>, @Part("tag") tag: RequestBody?): Call<ResultData<Any>>

    @Multipart
    @POST("file/insert")
    fun insert3(@PartMap map: Map<String, RequestBody>): Call<ResultData<Any>>

    @FormUrlEncoded
    @POST("account/info")
    fun info(@Field("code") code: Int, @Field("user") user: Int): Call<BodyData<ResultData<Any>>>

    /**
     * 登录
     * @param phone
     * @param verifyCode
     */
    @FormUrlEncoded
    @POST("/healthcare/api/login/login")
    fun loginAccount(@Field("phone") phone: String, @Field("verifyCode") code: String): Call<BodyData<ResultData<Any>>>

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST("account/logout")
    fun logoutAccount(): Call<BodyData<ResultData<Any>>>

    /**
     * 检测版本更新
     */
    @GET("config/checkVersion")
    fun checkVersion(@Query("curVersion") version: String): Call<BodyData<ResultData<Any>>>

    /**
     * 非接触体征综合检测
     * @param userId
     * @param file
     */
    @POST("/healthcare/api/vitalsigns/noContactSingns")
    fun noContactSing(@Query("id") id: Int): Call<ResultData<Any>>

    /**
     * 检测 -舌面
     * @param userId
     * @param file
     */
    @POST("/healthcare/api/facetongue/detectionHealth")
    fun detectionHealth(@Query("id") id: Int): Call<ResultData<Any>>

    /**
     *
     */
    fun get3(@Query("id") id: Int): Call<ResultData<Any>>

    /**
     *
     */
    fun get2(@Query("id") id: Int): Call<ResultData<Any>>


}