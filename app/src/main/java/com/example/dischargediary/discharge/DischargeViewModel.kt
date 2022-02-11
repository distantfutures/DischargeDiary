package com.example.dischargediary.discharge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabaseDao
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class DischargeViewModel(
    disType: Int = 0,
    private val database: DischargeDatabaseDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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
        getCurrentDate()
        getCurrentTime()
        _dischargeConsist.value = "N/A"
    }
    private fun unfilled(): Boolean {
        return if (dischargeType.value == 1) {
            !(dischargeType.value == 0 || dischargeDurationTime.value == null || leakageYN.value == null || dischargeColor.value == null)
        } else {
            !(dischargeDurationTime.value == null ||  leakageYN.value == null || dischargeColor.value == null || dischargeConsist.value == "N/A")
        }
    }
    fun onSubmitInfo() {
        uiScope.launch {
            var entryFilled = unfilled()
            if (entryFilled) {
                withContext(Dispatchers.IO) {
                    val newEntry = setDischargeData()
                    insertNewEntry(newEntry)
                    // Checks entry
                    val checkEntry = database.getRecentDischarge()
                    Log.d("CheckDischargeViewModel", "Submit $checkEntry")
                }
                _navigateToDiary.value = true
            } else {
                _navigateToDiary.value = false
            }
        }
    }
    private fun setDischargeData(): DischargeData {
        val newEntry = DischargeData()
        newEntry.dischargeType = dischargeType.value!!
        newEntry.dischargeDate = dischargeDate.value!!
        newEntry.dischargeTime = dischargeTime.value!!
        newEntry.dischargeDuration = dischargeDurationTime.value!!
        newEntry.leakage = leakageYN.value!!
        newEntry.dischargeColor = dischargeColor.value!!
        newEntry.dischargeConsistency = dischargeConsist.value!!
        return newEntry
    }
    // Takes new initialized entry & adds to database
    private suspend fun insertNewEntry(newEntry: DischargeData) {
        withContext(Dispatchers.IO) {
            database.addNew(newEntry)
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
    }
    private fun colorConverter(group: Int, colorNumber: Int?): String? {
        val colorName : String?
        if (group != 2) {
            colorName = when (colorNumber) {
                1 -> "Clear"
                2 -> "Light Yellow"
                3 -> "Yellow"
                4 -> "Dark Yellow"
                5 -> "Red"
                else -> { null }
            }
        } else {
            colorName = when (colorNumber) {
                1 -> "Light Grey"
                2 -> "Brown"
                3 -> "Dark Brown/Black"
                4 -> "Green"
                5 -> "Red Stool"
                else -> { null }
            }
        }
        return colorName
    }
    fun onSetDischargeConsist(consist: String?) {
        _dischargeConsist.value = consist
        Log.i("CheckDischargeViewModel", "onSetDischargeConsist $consist")
    }

    fun onSetDischargeDuration(durationTime: String?) {
        _dischargeDurationTime.value = durationTime
    }

    fun doneNavigating() {
        _navigateToDiary.value = null
    }
    private fun getCurrentDate() {
        val currentDate = Calendar.getInstance()
        startYear = currentDate.get(Calendar.YEAR)
        startMonth = currentDate.get(Calendar.MONTH)
        startDay = currentDate.get(Calendar.DAY_OF_MONTH)
        currentDate.set(startYear, startMonth, startDay)
        val formatterDate = SimpleDateFormat("MM.dd.yyyy, EEE", Locale.getDefault())
        _dischargeDate.value = formatterDate.format(currentDate.time)
        Log.i("CheckViewModel", "$startYear $startMonth $startDay")
    }
    private fun getCurrentTime() {
        val currentTime = Calendar.getInstance()
        startHour = currentTime.get(Calendar.HOUR_OF_DAY)
        startMinute = currentTime.get(Calendar.MINUTE)
        currentTime.set(startHour, startMinute)
        val formatterTime = SimpleDateFormat("h:mm a", Locale.getDefault())
        _dischargeTime.value = formatterTime.format(currentTime.time)
        Log.i("CheckViewModel", "$startHour $startMinute")
    }
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
    }
//    fun pickADate(year: Int, month: Int, day: Int) {
//        val pickDate = Calendar.getInstance()
//        pickYear = year
//        pickMonth = month
//        pickDay = day
//        pickDate.set(pickYear, pickMonth, pickDay)
//        val formatterDate = SimpleDateFormat("MM.dd.yyyy, EEE", Locale.getDefault())
//        _dischargeDate.value = formatterDate.format(pickDate.time).toString()
//    }
//    fun pickATime(hour: Int, minute: Int) {
//        val pickTime = Calendar.getInstance()
//        pickHour = hour
//        pickMinute = minute
//        pickTime.set(0, 0, 0, pickHour, pickMinute)
//        val formatterTime = SimpleDateFormat("h:mm a", Locale.getDefault())
//        _dischargeTime.value = formatterTime.format(pickTime.time).toString()
//    }
}
