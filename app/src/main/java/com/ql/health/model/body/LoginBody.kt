package com.ql.health.model.body

import com.ql.health.config.Consts

class LoginBody(
    var phone: String,
    var verifyCode: String,
    var realName: String,
    var birthday: String,
    var gender: Int,
    var height: Int,
    var weight: Int,
) {
    val deviceSn: String = Consts.DEVICE_SN
    override fun toString(): String {
        return "LoginBody(phone='$phone', verifyCode='$verifyCode')"
    }
}