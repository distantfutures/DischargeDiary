package com.example.dischargediary.data

import androidx.lifecycle.LiveData

class DischargeRepository(private val dischargeDatabaseDao: DischargeDatabaseDao) {

    val getAllDischargeEntries: LiveData<List<DischargeData>> = dischargeDatabaseDao.getAllDischarges()

    suspend fun addNewEntry(discharge: DischargeData) {
        dischargeDatabaseDao.addNew(discharge)
    }
}