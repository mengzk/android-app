package com.dx.health.module.common.log

/**
 * Author: Meng
 * Date: 2023/07/21
 * Modify: 2023/07/21
 * Desc:
 */
data class LogDao(
    val url: String,
    val method: String,
    val code: Int,
    val time: Long,
    val params: String,
    val headers: Map<String, Any>,
    val result: String?
) {}