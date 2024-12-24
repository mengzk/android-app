package com.ql.health.model.entity

data class TongueEntity(
    val inquiry_questions: List<InquiryQuestion>,
    val session_id: String
)

data class InquiryQuestion(
    val name: String,
    val value: String
)