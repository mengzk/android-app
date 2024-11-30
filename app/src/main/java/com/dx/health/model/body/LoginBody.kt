package com.dx.health.model.body

class LoginBody(
    var phone: String,
    var verifyCode: String
) {
    override fun toString(): String {
        return "LoginBody(phone='$phone', verifyCode='$verifyCode')"
    }
}