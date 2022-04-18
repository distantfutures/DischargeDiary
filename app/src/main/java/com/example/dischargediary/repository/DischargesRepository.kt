package com.example.dischargediary.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DischargesRepository(private val database: DischargeDatabase) {
    val allDischarges: LiveData<List<DischargeData>> = database.dischargeDatabaseDao.getAllDischarges()

    suspend fun deleteEntryNumber(disMilliId:Long) {
        withContext(Dispatchers.IO) {
            database.dischargeDatabaseDao.deleteEntryNumber(disMilliId)
            Log.d("CheckDiaryVM", "Delete Entry Number! $disMilliId")
        }
    }
    suspend fun clearDiary() {
        withContext(Dispatchers.IO) {
            database.dischargeDatabaseDao.clear()
        }
    }
}