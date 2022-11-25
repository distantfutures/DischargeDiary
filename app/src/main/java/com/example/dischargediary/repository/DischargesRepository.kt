package com.example.dischargediary.repository

import androidx.lifecycle.LiveData
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class DischargesRepository(private val database: DischargeDatabase) {
    val allDischarges: LiveData<List<DischargeData>> = database.dischargeDatabaseDao.getAllDischarges()

    suspend fun insertNewEntry(newEntry: DischargeData) {
        withContext(Dispatchers.IO) {
            database.dischargeDatabaseDao.addNew(newEntry)
            Timber.d("Room: $newEntry")
        }
    }

    suspend fun deleteEntryNumber(disMilliId:Long) {
        withContext(Dispatchers.IO) {
            database.dischargeDatabaseDao.deleteEntryNumber(disMilliId)
            Timber.d("Delete Entry Number! $disMilliId")
        }
    }

    suspend fun clearDiary() {
        withContext(Dispatchers.IO) {
            database.dischargeDatabaseDao.clearAll()
        }
    }

    suspend fun getEntry(entry: Long) {
        withContext(Dispatchers.IO) {
            database.dischargeDatabaseDao.get(entry)
        }
    }
}