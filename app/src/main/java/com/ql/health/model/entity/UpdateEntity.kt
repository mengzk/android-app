package com.ql.health.model.entity

data class UpdateEntity(
    val createTime: String,
    val downloadUrl: String,
    val forcedUpdate: Int,
    val id: Int,
    val isDelete: Int,
    val onlineTime: String,
    val os: Int,
    val updateTime: String,
    val version: String,
    val versionDesc: String,
    val versionSeq: Int
)