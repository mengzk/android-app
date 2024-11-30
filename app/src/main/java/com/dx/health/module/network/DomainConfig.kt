package com.dx.health.module.network

/**
 * Author: Meng
 * Date: 2022/09/13
 * Desc:
 */

object DomainConfig {
    fun getDomain(env: String): String {
        return when (env) {
            "test" -> "https://192.168.253.109:8093"
            "dev" -> "http://192.168.253.109:8093"
            else -> "https://def.prod.com"
        }
    }
}