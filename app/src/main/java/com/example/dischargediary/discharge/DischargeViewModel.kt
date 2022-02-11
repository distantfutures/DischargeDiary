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
        _dischargeDate.value = getCurrentDate()
        _dischargeTime.value = getCurrentTime()
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
                    // Initializes new entry, Assigns data, Insert into database
                    val newEntry = DischargeData()

                    newEntry.dischargeType = dischargeType.value!!
                    newEntry.dischargeDate = dischargeDate.value!!
                    newEntry.dischargeTime = dischargeTime.value!!
                    newEntry.dischargeDuration = dischargeDurationTime.value!!
                    newEntry.leakage = leakageYN.value!!
                    newEntry.dischargeColor = dischargeColor.value!!
                    newEntry.dischargeConsistency = dischargeConsist.value!!

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
    // Takes new initialized entry
    private suspend fun insertNewEntry(newEntry: DischargeData) {
        withContext(Dispatchers.IO) {
            database.addNew(newEntry)
        }
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

    fun getNewDate(date: String?) {
        _dischargeDate.value = date
    }

    fun getNewTime(time: String?) {
        _dischargeTime.value = time
    }

    fun onSetDischargeType(dischargeOneTwo: Int) {
        _dischargeType.value = dischargeOneTwo
    }

    fun onSetLeakageYN(leakYN: Boolean) {
        _leakageYN.value = leakYN
    }

    fun onSetDischargeColor(colorNumber: Int) {
        _dischargeColor.value = colorConverter(colorNumber)
    }
    private fun colorConverter(colorNumber: Int?): String? {
        val colorName: String? = when (colorNumber) {
            1 -> "Clear"
            2 -> "Light Yellow"
            3 -> "Yellow"
            4 -> "Dark Yellow"
            5 -> "Red"
            6 -> "Light Grey"
            7 -> "Brown"
            8 -> "Dark Brown/Black"
            9 -> "Green"
            10 -> "Red Stool"
            else -> { null }
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
}
