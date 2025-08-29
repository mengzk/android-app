package com.ql.health.model.body

data class PersonalBody(
    var realName: String="",
    var birthYear: Int=2000,
    var gender: Int=0, // 1: 男 2: 女
    var height: Int=0,
    var weight: Int=0,
) {
}