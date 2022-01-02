package com.example.dischargediary.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DischargeDatabaseDao {
    @Insert
        fun addNew(entryId: DischargeData)
    @Update
        fun update(entryId: DischargeData)
    @Query("SELECT * from discharge_diary_table WHERE entryId = :key")
        fun get(key: Int): DischargeData?
    @Query("DELETE FROM discharge_diary_table")
        fun clear()
    @Query("SELECT * FROM discharge_diary_table ORDER BY entryId DESC")
        fun getAllDischarges(): LiveData<List<DischargeData>>
    @Query("SELECT * FROM discharge_diary_table ORDER BY entryId DESC LIMIT 1")
        fun getRecentDischarge(): DischargeData?
}