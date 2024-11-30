package com.dx.health.module.common.network

import com.dx.health.module.common.exception.CoroutineHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Meng
 * Date: 2023/08/03
 * Modify: 2023/08/03
 * Desc:
 */
object NetIO {

    /**
     * 在协程作用域中切换至IO线程
     */
    suspend fun <T> on(block: suspend () -> T): T {
        return withContext(Dispatchers.IO + CoroutineHandler) {
            block.invoke()
        }
    }
}