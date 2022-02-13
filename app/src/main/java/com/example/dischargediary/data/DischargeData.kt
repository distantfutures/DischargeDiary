package com.example.dischargediary.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discharge_diary_table")
data class DischargeData(

    @PrimaryKey(autoGenerate = true)
    var entryId: Int = 0,

    @ColumnInfo(name = "discharge_type")
    var dischargeType: Int = 0,

    @ColumnInfo(name = "discharge_date")
    var dischargeDate: String = "",

    @ColumnInfo(name = "discharge_time")
    var dischargeTime: String = "",

    @ColumnInfo(name = "discharge_milli")
    var dischargeMilli: Long = 0L,

    @ColumnInfo(name = "discharge_duration")
    var dischargeDuration: String = "",

    @ColumnInfo(name = "leakage")
    var leakage: Boolean = false,

    @ColumnInfo(name = "discharge_color")
    var dischargeColor: String = "",

    @ColumnInfo(name = "discharge_consistency")
    var dischargeConsistency: String = ""
)
