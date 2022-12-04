package com.example.dischargediary.discharge

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dischargediary.UiSetter
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.repository.DischargesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DischargeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var uiSet: UiSetter = UiSetter()
    private val dischargesRepository =
        DischargesRepository(DischargeDatabase.getInstance(context))

    var startYear = 0
    var startMonth = 0
    var startDay = 0
    var startHour = 0
    var startMinute = 0

    // TODO: Use dependency injection here to create a singleton
    private val _dischargeType = MutableLiveData<Int>()
    val dischargeType: LiveData<Int> = _dischargeType

    private val _dischargeDate = MutableLiveData<String?>()
    val dischargeDate: LiveData<String?> = _dischargeDate

    private val _dischargeTime = MutableLiveData<String?>()
    val dischargeTime: LiveData<String?> = _dischargeTime

    private val _navigateToDiary = MutableLiveData<Boolean?>()
    val navigateToDiary: LiveData<Boolean?> = _navigateToDiary

    private var dischargeMilli: Long = 0L
    private var leakageYN: Boolean = false
    var dischargeColorButton: Int = 0
    private var dischargeColor: String? = null
    private var dischargeConsist: String? = null
    private var dischargeDurationTime: String? = null

    init {
        getCurrentDateTime()
    }

    // Checks if any inputs are unfilled
    private fun unfilled(): Boolean {
        return if (dischargeType.value == 1) {
            (dischargeType.value == 0 || dischargeDurationTime == null || dischargeColor == null)
        } else {
            (dischargeDurationTime == null || dischargeColor == null || dischargeConsist == null)
        }
    }

    // Sets all values to new discharge entry object
    private fun setDischargeData(): DischargeData {
        return DischargeData(
            dischargeType = _dischargeType.value!!,
            dischargeDate = dischargeDate.value!!,
            dischargeTime = dischargeTime.value!!,
            dischargeMilli = dischargeMilli,
            dischargeDuration = dischargeDurationTime!!,
            leakage = leakageYN,
            dischargeColor = dischargeColor!!,
            dischargeConsistency = dischargeConsist!!
        )
    }

    // Inserts entry into Room Database & navigates back to Diary if entry is filled
    fun onSubmitInfo() {
        viewModelScope.launch {
            if (unfilled()) {
                _navigateToDiary.value = false
            } else {
                val newEntry = setDischargeData()
                dischargesRepository.insertNewEntry(newEntry)
                _navigateToDiary.value = true
            }
        }
    }

    fun onSetDischargeType(dischargeOneTwo: Int) {
        _dischargeType.value = dischargeOneTwo
    }

    fun onSetLeakageYN(leakYN: Boolean) {
        leakageYN = leakYN
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSetDischargeConsist(consist: Int) {
        val consistString = context.getString(uiSet.stringConsistRef(consist))
        dischargeConsist = consistString
    }

    fun onSetDischargeDuration(durationTime: String?) {
        dischargeDurationTime = durationTime
    }

    // Converts and sets from dischargeColor button
    fun onSetDischargeColor(buttonNumber: Int) {
        dischargeColorButton = buttonNumber
        dischargeColor = uiSet.mapButtonNumberToColors(dischargeType.value!!, buttonNumber, context)
    }

    fun doneNavigating() {
        _navigateToDiary.value = null
    }

    // Gets initial DateTime from Fragment DateTimePickerDialog
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
    }

    // Gets New DateTime from Fragment DateTimePickerDialog
    @RequiresApi(Build.VERSION_CODES.O)
    fun pickADateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val pickDateTime = Calendar.getInstance()
        pickDateTime.set(year, month, day, hour, minute)
        val formatterDate = SimpleDateFormat("MM.dd.yyyy, EEE", Locale.getDefault())
        val formatterTime = SimpleDateFormat("h:mm a", Locale.getDefault())
        _dischargeDate.value = formatterDate.format(pickDateTime.time).toString()
        _dischargeTime.value = formatterTime.format(pickDateTime.time).toString()
        milliDateTimeFormatter(pickDateTime)
    }

    // Sets newly formatted date to LiveData
    @RequiresApi(Build.VERSION_CODES.O)
    fun milliDateTimeFormatter(dateTime: Calendar) {
        val simpleFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val simpleFormat = simpleFormatter.format(dateTime.time).toString()
        val localDate: LocalDateTime = LocalDateTime.parse(simpleFormat, formatter)
        val timeMilli: Long = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
        dischargeMilli = timeMilli
    }
}
