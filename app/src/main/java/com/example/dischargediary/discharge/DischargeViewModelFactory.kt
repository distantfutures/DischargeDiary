package com.example.dischargediary.discharge

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dischargediary.data.DischargeDatabaseDao

class DischargeViewModelFactory(
    private val disType: Int,
    private val datasource: DischargeDatabaseDao) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DischargeViewModel::class.java)) {
            return DischargeViewModel(disType, datasource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}