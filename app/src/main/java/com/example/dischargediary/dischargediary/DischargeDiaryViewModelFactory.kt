package com.example.dischargediary.dischargediary

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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
