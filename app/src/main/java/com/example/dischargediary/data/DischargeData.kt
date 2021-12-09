package com.example.dischargediary.data

data class DischargeData(
    var dischargeDate: String = "",
    var dischargeTime: String = "",
    var dischargeType: Int = 0,
    var dischargeDuration: String = "",
    var leakage: Boolean = false,
    var dischargeColor: String = "",
    var dischargeConsistency: String = ""
)
