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

    private val _dischargeColorNumber = MutableLiveData<Int>()
    val dischargeColorNumber: LiveData<Int>
        get() = _dischargeColorNumber

    private val _dischargeConsist = MutableLiveData<String?>()
    val dischargeConsist: LiveData<String?>
        get() = _dischargeConsist

    private val _dischargeDurationTime = MutableLiveData<String?>()
    val dischargeDurationTime: LiveData<String?>
        get() = _dischargeDurationTime

    private val _convertedColor = MutableLiveData<String?>()
    val convertedColor: LiveData<String?>
        get() = _convertedColor

    init {
        _dischargeType.value = 0
        _dischargeColorNumber.value = 0
    }

    fun onSetDischargeType(dischargeOneTwo: Int) {
        _dischargeType.value = dischargeOneTwo
        //showToast(dischargeType.toString())
    }

    fun onSetLeakageYN(leakYN: Boolean) {
        _leakageYN.value = leakYN
    }

    fun onSetDischargeColor(colorNumber: Int) {
        _dischargeColorNumber.value = colorNumber
        colorConverter(_dischargeColorNumber.value)
    }

    fun onSetDischargeConsist(consist: String?) {
        _dischargeConsist.value = consist
    }

    fun onSetDischargeDuration(durationTime: String?) {
        _dischargeDurationTime.value = durationTime
    }

    fun colorConverter(colorNumber: Int?) {
        _dischargeColorNumber.value = colorNumber!!
        val colorName = when (colorNumber) {
            0 -> "Need Value"
            1 -> "Clear"
            2 -> "Light Yellow"
            3 -> "Yellow"
            4 -> "Dark Yellow"
            5 -> "Light Brown"
            6 -> "Brown"
            7 -> "Green"
            8 -> "Black"
            else -> { "Other" }
        }
        _convertedColor.value = colorName
    }
}
