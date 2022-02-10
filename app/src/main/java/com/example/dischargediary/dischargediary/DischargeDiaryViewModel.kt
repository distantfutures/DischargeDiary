package com.example.dischargediary.dischargediary

import android.app.Application
import androidx.lifecycle.*
import com.example.dischargediary.data.DischargeDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DischargeDiaryViewModel(
    private val database: DischargeDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val getAllDischarges = database.getAllDischarges()

    private val _dischargeDateTime = MutableLiveData<String?>()
    val dischargeDateTime: LiveData<String?>
        get() = _dischargeDateTime

    private val _dischargeTypeArg = MutableLiveData<Int>()
    val dischargeTypeArg: LiveData<Int>
        get() = _dischargeTypeArg

    val clearButtonVisible = Transformations.map(getAllDischarges) {
        it?.isNotEmpty()
    }

    init {
        _dischargeDateTime.value = getCurrentDateTime()
    }

    // Sets DischargeType when navigating to DischargeEntryFragment
    fun onNewEntry(disType: Int) {
        _dischargeTypeArg.value = disType
    }

    private suspend fun deleteEntryNumber(entryId: Int) {
        withContext(Dispatchers.IO) {
            database.deleteEntryNumber(entryId)
        }
    }

    private fun getCurrentDateTime(): String? {
        // Get the current time (in millis)
        val now = Date().time
        // Create a formatter along with the desired output pattern
        val formatter = SimpleDateFormat("EEEE,  MMMM dd, yyyy @ hh:mm a", Locale.getDefault())
        // Put the time (in millis) in our formatter
        val result = formatter.format(now)
        return result
    }

    fun doneNavigating() {
        _dischargeTypeArg.value = 0
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
        }
    }
}