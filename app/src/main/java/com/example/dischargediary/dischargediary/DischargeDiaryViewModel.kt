package com.example.dischargediary.dischargediary

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabaseDao
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class DischargeDiaryViewModel(
    val database: DischargeDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var recentDischarge = MutableLiveData<DischargeData?>()

    val getAllDischarges = database.getAllDischarges()

//    val dischargeEntriesString = Transformations.map(getAllDischarges) { discharges ->
//        formatDischarges(discharges, application.resources)
//    }

    private val _dischargeDateTime = MutableLiveData<String?>()
    val dischargeDateTime: LiveData<String?>
        get() = _dischargeDateTime

    private val _navigateToDischargeEntry = MutableLiveData<DischargeData?>()
    val navigateToDischargeEntry: LiveData<DischargeData?>
        get() = _navigateToDischargeEntry

    val clearButtonVisible = Transformations.map(getAllDischarges) {
        it?.isNotEmpty()
    }

    init {
        _dischargeDateTime.value = getCurrentDateTime()
    }

    private suspend fun getEntryfromDatabase(): DischargeData? {
        return withContext(Dispatchers.IO) {
            var entry = database.getRecentDischarge()
            if (entry?.dischargeType == 0) {
                entry = null
            }
            entry
        }
    }
    //inserts new entry into database
    fun onNewEntry(type: Int) {
        uiScope.launch {
            val newEntry = DischargeData()
            insertEntry(newEntry, type)
            recentDischarge.value = getEntryfromDatabase()
            _navigateToDischargeEntry.value = getEntryfromDatabase()
            Log.d("NewEntry", "New Entry received ${recentDischarge.value?.dischargeType}")
        }
    }
    private suspend fun insertEntry(entryId: DischargeData, type: Int) {
        withContext(Dispatchers.IO) {
            database.addNew(entryId)
            val insert = database.getRecentDischarge() ?: return@withContext
            insert.dischargeType = type
            database.update(insert)
            Log.d("NewEntry", "Insert received ${database.getRecentDischarge()}")
        }
    }
    private suspend fun deleteEntryNumber(entryId: Int) {
        withContext(Dispatchers.IO) {
            database.deleteEntryNumber(entryId)
        }
    }

    fun getCurrentDateTime(): String? {
        // Get the current time (in millis)
        val now = Date().time

        // Create a formatter along with the desired output pattern
        val formatter = SimpleDateFormat("EEEE,  MMMM dd ''yy @ hh:mm a", Locale.getDefault())

        // Put the time (in millis) in our formatter
        val result = formatter.format(now)

        return result
    }

    fun doneNavigating() {
        _navigateToDischargeEntry.value = null
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