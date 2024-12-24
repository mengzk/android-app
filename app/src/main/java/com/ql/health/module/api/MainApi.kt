package com.ql.health.module.api

import com.ql.health.model.body.*
import com.ql.health.model.entity.*
import com.ql.health.module.common.network.BodyData
import com.ql.health.module.common.network.ResultData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


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
//    fun detail(@Header("textHead") String test, @Query("id") id: Int): Call<ResultData<Any>>

//    @POST("open/model/{id}/invoke")
//    fun chatModelV3Api(@Path("id") id: String, @Body body: TestBody): Call<ResultData<Any>>

    @Multipart
    @POST("healthcare/api/upload/upload")
    fun upload(@Part file: MultipartBody.Part): Call<BodyData<Any>>

    @Multipart
    @POST("file/upload")
    fun upload3(@Part file: MultipartBody.Part, @Part("userId") tag: RequestBody?): Call<ResultData<Any>>

    @Multipart
    @POST("file/uploads")
    fun uploads3(@Part files: List<MultipartBody.Part>, @Part("tag") tag: RequestBody?): Call<ResultData<Any>>

    @Multipart
    @POST("file/insert")
    fun insert3(@PartMap map: Map<String, RequestBody>): Call<ResultData<Any>>

    @Multipart
    @POST("file/uploadMap")
    fun uploadMap(@Part files: List<MultipartBody.Part>, @Part("scene") scene: RequestBody, @Part("userId") userId: RequestBody): Call<BodyData<Any>>

    @FormUrlEncoded
    @POST("account/info")
    fun info(@Field("code") code: Int, @Field("user") user: Int): Call<BodyData<Any>>

    /**
     * 登录
     * @param phone
     * @param verifyCode
     */
//    @FormUrlEncoded
    @POST("healthcare/api/login/login")
    fun loginAccount(@Body body: LoginBody): Call<BodyData<UserEntity>>

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST("account/logout")
    fun logoutAccount(): Call<BodyData<Any>>

    /**
     * 检测版本更新
     */
    @GET("healthcare/appversion/checkVesion")
    fun checkVersion(@Query("version") version: String, @Query("os") os: Long): Call<BodyData<Any>>

    /**
     * 阶梯脉搏检测
     */
    @POST("healthcare/api/healthDeviceDetection/submitSamplingData")
    fun pulseHealth(@Body body: PulseBody): Call<BodyData<String>>

    /**
     * 阶梯脉搏检测详情
     */
    @POST("healthcare/api/healthDeviceDetection/getSamplingReport")
    fun pulseHealthReport(@Body body: PulseReportBody): Call<BodyData<PulseReportEntity>>

    /**
     * 检测 -舌面
     * @param userId
     * @param file
     */
    @Multipart
    @POST("healthcare/api/facetongue/createFacetongue")
    fun faceTongueCheck(@Part files: List<MultipartBody.Part>, @Part("scene") scene: RequestBody, @Part("userId") userId: RequestBody): Call<BodyData<String>>

    /**
     * 检测 -舌面报告结果
     */
    @POST("healthcare/api/facetongue/detectionHealth")
    fun faceTongueReport(@Body body: TongueReportBody): Call<BodyData<Any>>

    /**
     *
     */
    fun test33(@Query("id") id: Int): Call<BodyData<Any>>
}