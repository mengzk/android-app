package com.ql.health.module.common.network

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
    val call = NetApis().main().detail(1001).enqueue(object : Callback<Any>{
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
object NetApis {

//    lateinit var mainApi: MainApi
//    lateinit var orderApi: OrderApi
//
//    private val builder: Retrofit.Builder = Retrofit.Builder()
//        .client(Network.client())
//        .addConverterFactory(MyGsonConverterFactory.create())
//
//    fun initApi() {
//        val orderUrl = Config.envHost.order
//        var retrofit = builder.baseUrl(orderUrl).build()
//        orderApi = retrofit.create(OrderApi::class.java)
//
//        val mainUrl = Config.envHost.def
//        retrofit = builder.baseUrl(mainUrl).build()
//        mainApi = retrofit.create(MainApi::class.java)
//    }
}