package com.ql.health.custom

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

    fun get(index: Int): Activity {
        return actStacks[index]
    }

    fun getTop(): Activity {
        return actStacks.lastElement()
    }

    fun size(): Int {
        return actStacks.size
    }

    fun close() {
        val size = actStacks.size
        for (i in 0 until size) {
            actStacks.pop().finish()
        }
    }


   fun top() {
        // 返回到顶部
        val size = actStacks.size
        for (i in 0 until size - 1) {
            actStacks.pop().finish()
        }
    }

//    fun clear() {
//        actStacks.clear()
//    }

}