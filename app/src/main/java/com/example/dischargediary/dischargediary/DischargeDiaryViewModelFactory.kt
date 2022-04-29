package com.example.dischargediary.dischargediary

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dischargediary.discharge.DischargeViewModel

class DischargeDiaryViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DischargeDiaryViewModel::class.java)) {
            return DischargeDiaryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DischargeViewModelFactory(
    private val disType: Int,
    private val application: Application) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DischargeViewModel::class.java)) {
            return DischargeViewModel(disType, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}