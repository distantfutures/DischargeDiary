package com.example.dischargediary.dischargediary

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.repository.DischargesRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DischargeDiaryViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dischargesRepository = DischargesRepository(DischargeDatabase.getInstance(application))
    val dischargeDiary = dischargesRepository.allDischarges

    private val _dischargeDateTime = MutableLiveData<String?>()
    val dischargeDateTime: LiveData<String?>
        get() = _dischargeDateTime

    private val _dischargeTypeArg = MutableLiveData<Int>()
    val dischargeTypeArg: LiveData<Int>
        get() = _dischargeTypeArg

    val clearButtonVisible = Transformations.map(dischargeDiary) {
        it?.isNotEmpty()
    }

    init {
        _dischargeDateTime.value = getCurrentDateTime()
    }

    // Sets DischargeType when navigating to DischargeEntryFragment
    fun onNewEntry(disType: Int) {
        _dischargeTypeArg.value = disType
    }

    fun deleteEntryFromRepository(disMilliId: Long) {
        //Add catch exception for null
        viewModelScope.launch {
            dischargesRepository.deleteEntryNumber(disMilliId)
            Log.d("CheckDiaryVM", "Delete Entry! $disMilliId")
        }
    }

    private fun getCurrentDateTime(): String? {
        // Get the current time (in millis)
        val now = Date().time
        // Create a formatter along with the desired output pattern
        val formatter = SimpleDateFormat("EEEE,  MMMM dd, yyyy @ hh:mm a", Locale.getDefault())
        // Put the time (in millis) in our formatter
        return formatter.format(now)
    }

    fun doneNavigating() {
        _dischargeTypeArg.value = 0
    }

    fun onClearFromRepository() {
        viewModelScope.launch {
            dischargesRepository.clearDiary()
        }
    }
}