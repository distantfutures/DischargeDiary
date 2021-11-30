package com.example.dischargediary.discharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DischargeViewModel() : ViewModel() {
//number: Int
    private val _dischargeType = MutableLiveData<Int>()
    val dischargeType: LiveData<Int>
        get() = _dischargeType

    private val _leakageYN = MutableLiveData<Boolean>()
    val leakageYN: LiveData<Boolean>
        get() = _leakageYN
    init {
        _dischargeType.value = 0
        _leakageYN.value = false
    }

    fun onSetDischargeType(dischargeOneTwo: Int) {
        _dischargeType.value = dischargeOneTwo
        //showToast(dischargeType.toString())
    }

    fun onSetLeakageYN(leakYN: Boolean) {
        _leakageYN.value = leakYN
    }
}