package com.ql.health.model.body

import com.ql.health.config.Consts

data class PulseBody(val array: ByteArray, val data: String, val signature: String, val hand: Int, val device_mac: String, val device_model: String, val sample_rate: Int, val blood_oxygen: Int) {
    var codec: String = "IR"
    val userId = Consts.USER_ID
}