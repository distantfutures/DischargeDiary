package com.example.dischargediary.discharge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dischargediary.data.DischargeDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*

class DischargeViewModel(
    private val dischargeKey: Long = 0L,
    val database: DischargeDatabaseDao
) : ViewModel() {

    override fun onCleared() {
        Log.d("DischargeFragment", "DischargeViewModel Destroyed")
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _dischargeDate = MutableLiveData<String?>()
    val dischargeDate: LiveData<String?>
        get() = _dischargeDate

    private val _dischargeTime = MutableLiveData<String?>()
    val dischargeTime: LiveData<String?>
        get() = _dischargeTime

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
        Log.d("DischargeFragment", "DischargeViewModel Created")
        _dischargeType.value = 0
        _dischargeColorNumber.value = 0
        _dischargeDate.value = getCurrentDate()
        _dischargeTime.value = getCurrentTime()
    }

    //NEW
    fun onSubmitInfo() {
        val discharge = database.get(dischargeKey) ?: return
        discharge.dischargeType = dischargeType.value
        discharge.dischargeDate = dischargeDate.value
        discharge.dischargeTime = dischargeTime.value
        database.update(discharge)
        //type: Int, date: String, time: String
    }

    fun getCurrentDate(): String? {
        // Get the current time (in millis)
        val now = Date().time
        // Create a formatter along with the desired output pattern
        val formatter = SimpleDateFormat("MM.dd.yyyy, EEE", Locale.getDefault())
        // Put the time (in millis) in our formatter
        val result = formatter.format(now)
        return result
    }

    fun getCurrentTime(): String? {
        // Get the current time (in millis)
        val now = Date().time
        // Create a formatter along with the desired output pattern
        val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
        // Put the time (in millis) in our formatter
        val result = formatter.format(now)
        return result
    }

//    fun getNewDateTime(dateTime: String?) {
//        _dischargeDateTime.value = dateTime
//    }

    fun getNewDate(date: String?) {
        _dischargeDate.value = date
    }

    fun getNewTime(time: String?) {
        _dischargeTime.value = time
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
