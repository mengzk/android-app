package com.ql.health.config

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
object Configs {
    private var env: String = "test"
    const val secret: String = ""
    const val apiId: String = ""
    const val h5Url: String = "https://www.aimlai.com/health/#/"

    fun getEnv(): String {
        return env
    }

    fun setEnv(env: String) {
        this.env = env
    }

    fun getDomain(env: String): String {
        return when (env) {
            "test" -> "https://aimlai.com"
            "dev" -> "http://192.168.31.141:8093"
            else -> "https://aimlai.com"
        }
    }
}
