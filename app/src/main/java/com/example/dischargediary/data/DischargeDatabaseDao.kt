package com.example.dischargediary.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DischargeDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun addNew(dischargeId: DischargeData)
    @Update
        fun update(dischargeId: DischargeData)
    @Query ("SELECT * from discharge_diary_table WHERE dischargeId = :key")
        fun get(key: Int): DischargeData?
    @Query ("DELETE FROM discharge_diary_table")
        fun clear()
    @Query ("SELECT * FROM discharge_diary_table ORDER BY dischargeId DESC")
        fun getAllDischarges(): LiveData<List<DischargeData>>
    @Query ("SELECT * FROM discharge_diary_table ORDER BY dischargeId DESC LIMIT 1")
        fun getRecentDischarge(): DischargeData?
}