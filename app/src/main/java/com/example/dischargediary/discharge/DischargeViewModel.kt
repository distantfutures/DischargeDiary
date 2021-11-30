package com.example.dischargediary.discharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DischargeViewModel() : ViewModel() {
//number: Int
    private val _dischargeType = MutableLiveData<Int?>()
    val dischargeType: LiveData<Int?>
        get() = _dischargeType

    private val _leakageYN = MutableLiveData<Boolean?>()
    val leakageYN: LiveData<Boolean?>
        get() = _leakageYN

    private val _dischargeColor = MutableLiveData<Int>()
    val dischargeColor: LiveData<Int>
        get() = _dischargeColor

    private val _dischargeConsist = MutableLiveData<String?>()
    val dischargeConsist: LiveData<String?>
        get() = _dischargeConsist

    init {
        _dischargeType.value = 0
        _dischargeColor.value = 0
    }

    fun onSetDischargeType(dischargeOneTwo: Int) {
        _dischargeType.value = dischargeOneTwo
        //showToast(dischargeType.toString())
    }

    fun onSetLeakageYN(leakYN: Boolean) {
        _leakageYN.value = leakYN
    }

    fun onSetDischargeColor(colorNumber: Int) {
        _dischargeColor.value = colorNumber
    }

    fun onSetDischargeConsist(consist: String?) {
        _dischargeConsist.value = consist
    }
}
