package com.example.dischargediary.discharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dischargediary.data.DischargeDatabaseDao

class DischargeViewModelFactory(
    private val dischargeKey: Int,
    private val datasource: DischargeDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DischargeViewModelFactory::class.java)) {
            return DischargeViewModel(dischargeKey, datasource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}