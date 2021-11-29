package com.example.dischargediary.discharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DischargeViewModel() : ViewModel() {
    private val _dischargeType = MutableLiveData<Int>()
    val dischargeType: LiveData<Int>
        get() = _dischargeType

    init {
        _dischargeType.value = 0
    }
}
