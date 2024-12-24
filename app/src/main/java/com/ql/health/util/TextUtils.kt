package com.ql.health.util

/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
object TextUtils {
    fun isEmpty(str: String?): Boolean {
        return str.isNullOrEmpty()
    }

    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    fun isBlank(str: String?): Boolean {
        return str.isNullOrEmpty() || str.trim().isEmpty()
    }

    fun isNotBlank(str: String?): Boolean {
        return !isBlank(str)
    }

    fun equals(str1: String?, str2: String?): Boolean {
        return str1 == str2
    }

    fun isPhone(phone: String?): Boolean {
        return phone?.matches(Regex("^1[3-9]\\d{9}$")) ?: false
    }
}