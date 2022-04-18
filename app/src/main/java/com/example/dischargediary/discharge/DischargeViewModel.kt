package com.example.dischargediary.discharge

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dischargediary.R
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.repository.DischargesRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DischargeViewModel(
    disType: Int = 0,
    application: Application
) : AndroidViewModel(application) {

    private val dischargesRepository =
        DischargesRepository(DischargeDatabase.getInstance(application))

    var startYear = 0
    var startMonth = 0
    var startDay = 0
    var startHour = 0
    var startMinute = 0

    var pickYear = 0
    var pickMonth = 0
    var pickDay = 0
    var pickHour = 0
    var pickMinute = 0

    private val _dischargeDate = MutableLiveData<String?>()
    val dischargeDate: LiveData<String?>
        get() = _dischargeDate

    private val _dischargeTime = MutableLiveData<String?>()
    val dischargeTime: LiveData<String?>
        get() = _dischargeTime

    private val _dischargeMilli = MutableLiveData<Long?>()
    val dischargeMilli: LiveData<Long?>
        get() = _dischargeMilli

    private val _dischargeType = MutableLiveData<Int?>()
    val dischargeType: LiveData<Int?>
        get() = _dischargeType

    private val _leakageYN = MutableLiveData<Boolean?>()
    val leakageYN: LiveData<Boolean?>
        get() = _leakageYN

    private val _dischargeColorButton = MutableLiveData<Int?>()
    val dischargeColorButton: LiveData<Int?>
        get() = _dischargeColorButton

    private val _dischargeColor = MutableLiveData<String?>()
    val dischargeColor: LiveData<String?>
        get() = _dischargeColor

    private val _dischargeConsist = MutableLiveData<String?>()
    val dischargeConsist: LiveData<String?>
        get() = _dischargeConsist

    private val _dischargeDurationTime = MutableLiveData<String?>()
    val dischargeDurationTime: LiveData<String?>
        get() = _dischargeDurationTime

    private val _navigateToDiary = MutableLiveData<Boolean?>()
    val navigateToDiary: LiveData<Boolean?>
        get() = _navigateToDiary

    init {
        Log.d("CheckDischargeViewModel", "entryID Test $disType")
        _dischargeType.value = disType
        getCurrentDateTime()
        _dischargeConsist.value = "N/A"
    }

    private fun unfilled(): Boolean {
        return if (dischargeType.value == 1) {
            !(dischargeType.value == 0 || dischargeDurationTime.value == null || leakageYN.value == null || dischargeColor.value == null)
        } else {
            !(dischargeDurationTime.value == null || leakageYN.value == null || dischargeColor.value == null || dischargeConsist.value == "N/A")
        }
    }

    private fun setDischargeData(): DischargeData {
        val newEntry = DischargeData()
        newEntry.dischargeType = dischargeType.value!!
        newEntry.dischargeDate = dischargeDate.value!!
        newEntry.dischargeTime = dischargeTime.value!!
        newEntry.dischargeMilli = dischargeMilli.value!!
        newEntry.dischargeDuration = dischargeDurationTime.value!!
        newEntry.leakage = leakageYN.value!!
        newEntry.dischargeColor = dischargeColor.value!!
        newEntry.dischargeConsistency = dischargeConsist.value!!
        return newEntry
    }

    fun onSubmitInfo() {
        viewModelScope.launch {
            val entryFilled = unfilled()
            if (entryFilled) {
                val newEntry = setDischargeData()
                dischargesRepository.insertNewEntry(newEntry)
                _navigateToDiary.value = true
            } else {
                _navigateToDiary.value = false
            }
        }
    }

    fun onSetDischargeType(dischargeOneTwo: Int) {
        _dischargeType.value = dischargeOneTwo
    }

    fun onSetLeakageYN(leakYN: Boolean) {
        _leakageYN.value = leakYN
    }

    fun onSetDischargeColor(colorNumber: Int?) {
        _dischargeColorButton.value = colorNumber
        _dischargeColor.value = colorConverter(dischargeType.value!!, colorNumber)
        Log.i("CheckDischargeViewModel", "checkDischargeColor ${_dischargeColor.value}")
    }

    fun onSetDischargeConsist(consist: Int?) {
        val consistString = when (consist) {
            1 -> R.string.consist_one.toString()
            2 -> R.string.consist_two.toString()
            3 -> R.string.consist_three.toString()
            4 -> R.string.consist_four.toString()
            5 -> R.string.consist_five.toString()
            else -> {
                "N/A"
            }
        }
        _dischargeConsist.value = consistString
        Log.i("CheckDischargeViewModel", "onSetDischargeConsist $consist")
    }

    fun onSetDischargeDuration(durationTime: String?) {
        _dischargeDurationTime.value = durationTime
    }

    fun colorConverter(group: Int, colorNumber: Int?): String? {
        val colorName: String?
        if (group != 2) {
            colorName = when (colorNumber) {
                1 -> R.string.urine_color_one.toString()
                2 -> R.string.urine_color_two.toString()
                3 -> R.string.urine_color_three.toString()
                4 -> R.string.urine_color_four.toString()
                5 -> R.string.urine_color_five.toString()
                else -> {
                    null
                }
            }
        } else {
            colorName = when (colorNumber) {
                1 -> R.string.stool_color_one.toString()
                2 -> R.string.stool_color_two.toString()
                3 -> R.string.stool_color_three.toString()
                4 -> R.string.stool_color_four.toString()
                5 -> R.string.stool_color_five.toString()
                else -> {
                    null
                }
            }
        }
        return colorName
    }

    fun doneNavigating() {
        _navigateToDiary.value = null
    }

    private fun getCurrentDateTime() {
        val currentDateTime = Calendar.getInstance()
        startYear = currentDateTime.get(Calendar.YEAR)
        startMonth = currentDateTime.get(Calendar.MONTH)
        startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        startMinute = currentDateTime.get(Calendar.MINUTE)
        currentDateTime.set(startYear, startMonth, startDay, startHour, startMinute)
        val formatterDate = SimpleDateFormat("MM.dd.yyyy, EEE", Locale.getDefault())
        val formatterTime = SimpleDateFormat("h:mm a", Locale.getDefault())
        _dischargeDate.value = formatterDate.format(currentDateTime.time)
        _dischargeTime.value = formatterTime.format(currentDateTime.time)
        milliDateTimeFormatter(currentDateTime)
        Log.i("CheckViewModel", "$startYear $startMonth $startDay")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun pickADateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val pickDateTime = Calendar.getInstance()
        pickYear = year
        pickMonth = month
        pickDay = day
        pickHour = hour
        pickMinute = minute
        pickDateTime.set(pickYear, pickMonth, pickDay, pickHour, pickMinute)
        val formatterDate = SimpleDateFormat("MM.dd.yyyy, EEE", Locale.getDefault())
        val formatterTime = SimpleDateFormat("h:mm a", Locale.getDefault())
        _dischargeDate.value = formatterDate.format(pickDateTime.time).toString()
        _dischargeTime.value = formatterTime.format(pickDateTime.time).toString()
        milliDateTimeFormatter(pickDateTime)
        Log.i(
            "CheckViewModel",
            "calendarTime $pickDateTime Milli ${_dischargeMilli.value.toString()}"
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun milliDateTimeFormatter(dateTime: Calendar) {
        val simpleFormatter = SimpleDateFormat(
            "EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH
        )
        val formatter = DateTimeFormatter.ofPattern(
            "EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH
        )
        val simpleFormat = simpleFormatter.format(dateTime.time).toString()
        Log.i("CheckViewModel", "simpleFormat $simpleFormat")
        val localDate: LocalDateTime = LocalDateTime.parse(simpleFormat, formatter)
        val timeMilli: Long = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
        _dischargeMilli.value = timeMilli
    }
}
