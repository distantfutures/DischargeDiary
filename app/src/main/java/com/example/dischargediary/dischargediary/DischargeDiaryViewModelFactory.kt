package com.example.dischargediary.dischargediary

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//
//class DischargeDiaryViewModelFactory(
//    private val application: Application
//) : ViewModelProvider.Factory {
//    @Suppress("unchecked_cast")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(DischargeDiaryViewModel::class.java)) {
//            return DischargeDiaryViewModel(application) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

//@HiltViewModel
//class DischargeViewModelFactory @Inject constructor(
//    private val application: BaseApplication,
//    @Ignore
//    private val disType: Int
//    ) : ViewModelProvider.Factory {
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @Suppress("unchecked_cast")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(DischargeViewModel::class.java)) {
//            return DischargeViewModel(application, disType) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}