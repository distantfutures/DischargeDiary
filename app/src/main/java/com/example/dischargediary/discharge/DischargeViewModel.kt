package com.example.dischargediary.discharge

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dischargediary.DateTime
import com.example.dischargediary.UiSetter
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.repository.DischargesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DischargeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var uiSet: UiSetter = UiSetter()
    private val dischargesRepository =
        DischargesRepository(DischargeDatabase.getInstance(context))

    // TODO: Use dependency injection here to create a singleton
    private val _dischargeType = MutableLiveData<Int>()
    val dischargeType: LiveData<Int> = _dischargeType

    private val _dischargeDate = MutableLiveData<String?>()
    val dischargeDate: LiveData<String?> = _dischargeDate

    private val _dischargeTime = MutableLiveData<String?>()
    val dischargeTime: LiveData<String?> = _dischargeTime

    private val _startDateTime = MutableLiveData<DateTime>()
    val startDateTime: LiveData<DateTime> = _startDateTime

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

    private fun getCurrentDateTime() {
        val dateTime = DateTime()
        _dischargeDate.value = dateTime.currentDateTimeInstance().first
        _dischargeTime.value = dateTime.currentDateTimeInstance().second
        _startDateTime.value = dateTime
        dischargeMilli = dateTime.milliDateTimeFormatter(dateTime.currentDateTime)
    }

    fun pickNewDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val instanceDateTime = DateTime()
        val newDateTime = instanceDateTime.pickADateTime(year, month, day, hour, minute)
        _dischargeDate.value = newDateTime.first
        _dischargeTime.value = newDateTime.second
        dischargeMilli = instanceDateTime.milliDateTimeFormatter(instanceDateTime.pickedDateTime)
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
}
