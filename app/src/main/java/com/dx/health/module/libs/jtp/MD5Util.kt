package com.dx.health.module.libs.jtp

import java.math.BigInteger
import java.security.MessageDigest

/**
 * MD5加密工具
 */
class MD5Util {
    /**
     * 对byte类型的数组进行MD5加密
     */
    fun getMD5String(bytes: ByteArray): String {
        return bytes.md5()
    }

}

fun ByteArray.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this)).toString(16).padStart(32, '0')
}
