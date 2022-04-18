package com.example.dischargediary.discharge

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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