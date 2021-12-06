package com.example.dischargediary.dischargediary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class DischargeDiaryViewModel() : ViewModel() {

    private val _dischargeDateTime = MutableLiveData<String?>()
    val dischargeDateTime: LiveData<String?>
        get() = _dischargeDateTime

    init {
        _dischargeDateTime.value = getCurrentDateTime()
    }

    fun getCurrentDateTime(): String? {
        // Get the current time (in millis)
        val now = Date().time

        // Create a formatter along with the desired output pattern
        val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())

        // Put the time (in millis) in our formatter
        val result = formatter.format(now)

        return result
    }
}
