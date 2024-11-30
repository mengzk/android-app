package com.dx.health.custom

import android.app.Activity
import java.util.Stack

/**
 * Author: Meng
 * Date: 2023/08/05
 * Modify: 2023/08/05
 * Desc:
 */
object ActivityStack {
    private val actStacks = Stack<Activity>()

    fun add(activity: Activity) {
        actStacks.push(activity)
    }

    fun pop() {
        actStacks.pop()
    }

    fun peek(): Activity {
        return actStacks.peek()
    }

    fun remove(activity: Activity) {
        actStacks.remove(activity)
    }

}