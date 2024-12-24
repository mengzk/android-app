package com.ql.health.custom

import android.view.View

/**
 * Author: Meng
 * Date: 2024/12/04
 * Modify: 2024/12/04
 * Desc:
 */
abstract class OnShakeClick(private var shakeTime: Int = 600) : View.OnClickListener {
    private var lastClickTime: Long = 0

    override fun onClick(v: View?) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > shakeTime) {
            lastClickTime = currentTime
            onTap(v)
        }
    }

    abstract fun onTap(v: View?)
}