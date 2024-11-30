package com.dx.health.module.network

import com.dx.health.config.Configs
import com.dx.health.module.api.MainApi
import com.dx.health.module.common.network.NetClient
import com.dx.health.module.common.network.retrofit.MyGsonConverterFactory
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
    lateinit var main: MainApi

    private val builder: Retrofit.Builder = Retrofit.Builder()
        .client(NetClient.client())
        .addConverterFactory(MyGsonConverterFactory.create())

    init {
        onChangeEnv()
    }

    fun onChangeEnv() {
        val mainUrl =  DomainConfig.getDomain(Configs.env)
        val retrofit = builder.baseUrl(mainUrl).build()
        main = retrofit.create(MainApi::class.java)
    }
}