package com.example.dischargediary.discharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DischargeViewModel() : ViewModel() {
//number: Int
    private val _dischargeType = MutableLiveData<Int>()
    val dischargeType: LiveData<Int>
        get() = _dischargeType

    init {
        _dischargeType.value = 0
    }

    fun onSetDischargeType(dischargeType: Int) {
        _dischargeType.value = dischargeType
        //showToast(dischargeType.toString())
    }

//    fun showToast(str: String) {
//        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
//    }
}