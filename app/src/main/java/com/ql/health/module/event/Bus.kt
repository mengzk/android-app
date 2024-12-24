package com.ql.health.module.event

/**
 * Author: Meng
 * Date: 2024/12/03
 * Modify: 2024/12/03
 * Desc:
 */
object Bus {
    private val listeners = mutableMapOf<String, MutableList<BusListener>>()

    fun add(tag: String, listener: BusListener) {
        if (listeners.containsKey(tag)) {
            listeners[tag]?.add(listener)
        } else {
            listeners[tag] = mutableListOf(listener)
        }
    }

    fun remove(tag: String) {
        if (listeners.containsKey(tag)) {
            listeners.remove(tag)
        }
    }

    fun send(tag: String, data: Action) {
        if (listeners.containsKey(tag)) {
            listeners[tag]?.forEach {
                it.onEvent(data)
            }
        }
    }

    interface BusListener {
        fun onEvent(data: Action)
    }

    data class Action(val tag: String, val code: Int, val data: Any?){}
}