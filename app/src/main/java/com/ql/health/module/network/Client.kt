package com.ql.health.module.network

import com.ql.health.config.Configs
import com.ql.health.module.api.MainApi
import com.ql.health.module.common.network.NetClient
import com.ql.health.module.common.network.retrofit.MyGsonConverterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:

    Client.main.test(1).enqueue(object : Callback<Any>{
        override fun onFailure(call: retrofit2.Call<Any>, t: Throwable) {}

        override fun onResponse(call: retrofit2.Call<Any>, response: retrofit2.Response<Any>) {
            if (response.isSuccessful) {
                for ((name, value) in response.headers()) {
                    println("$name: $value")
                }
                println(response.body().toString())
            }
            println(call.request().url)
        }
    })
 */
object Client {
    val VideoType = "video/mp4".toMediaTypeOrNull()
    val ImageType = "image/*".toMediaTypeOrNull()
    val PNGType = "image/png".toMediaTypeOrNull()
    val TextType = "text/plain".toMediaTypeOrNull()
    val JsonType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val FormType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
    val StreamType = "application/octet-stream".toMediaTypeOrNull()
    val FormData = "multipart/form-data".toMediaTypeOrNull()

    lateinit var main: MainApi

    private val builder: Retrofit.Builder = Retrofit.Builder()
        .client(NetClient.client())
        .addConverterFactory(MyGsonConverterFactory.create())

    init {
        getClient()
    }

    fun setClientEnv(env: String) {
        Configs.setEnv(env)
        getClient()
    }

    private fun getClient() {
        val mainUrl =  Configs.getDomain(Configs.getEnv())
        val retrofit = builder.baseUrl(mainUrl).build()
        main = retrofit.create(MainApi::class.java)
    }
}