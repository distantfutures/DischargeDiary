package com.example.dischargediary.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DischargeDatabaseDao {
    @Insert
        fun addNew(dischargeMilli: DischargeData)
    @Update
        fun update(entryId: DischargeData)
    @Query("SELECT * from discharge_diary_table WHERE dischargeMilli = :key")
        fun get(key: Long): DischargeData?
    @Query("DELETE FROM discharge_diary_table")
        fun clear()
    @Query("DELETE FROM discharge_diary_table WHERE dischargeMilli = :key")
        fun deleteEntryNumber(key: Long)
    @Query("SELECT * FROM discharge_diary_table ORDER BY dischargeMilli DESC")
        fun getAllDischarges(): Flow<List<DischargeData>>
}
