package com.ql.health.model.entity

data class PulseReportEntity(
    val has_questions: Any,
    val is_complete_report: Boolean,
    val report: ReportData
)

data class ReportData(
    val blood_oxygen: Int,
    val create_time: String,
    val health_age: Int,
    val health_report_id: String,
    val health_risk_index: HealthRiskIndex,
    val heart_rate: Int,
    val meridians: List<Meridian>,
    val physical_dialectics_list: List<PhysicalDialectics>,
    val physique: String,
    val report_summary: String,
    val risk: List<Risk>,
    val user_profile: UserProfile,
    val wave_data: List<Int>
)

data class HealthRiskIndex(
    val degree: String,
    val index_value: Int,
    val risk_id: String,
    val risk_name: String
)

data class Meridian(
    val date: Int,
    val describe: Describe,
    val name: String
)

data class PhysicalDialectics(
    val describe: DescribeX,
    val physical_dialectics_cate_content: String,
    val physical_dialectics_cate_key: String
)

data class Risk(
    val degree: String,
    val describe: DescribeXX,
    val index_value: Int,
    val risk_id: String,
    val risk_name: String
)

data class UserProfile(
    val age: Int,
    val gender: Int,
    val hand: Int,
    val height: Int,
    val user_profile_id: String,
    val weight: Int
)

data class Describe(
    val describe: String,
    val performance: String,
    val reason: String,
    val summary: String,
    val walk: String
)

data class DescribeX(
    val conditioning: String,
    val signs: String
)

data class DescribeXX(
    val describe: String,
    val reason: String,
    val refer: String
)