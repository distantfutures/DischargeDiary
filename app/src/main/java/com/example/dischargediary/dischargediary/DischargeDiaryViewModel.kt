package com.example.dischargediary.dischargediary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val discharges = database.getAllDischarges()

    private val _dischargeDateTime = MutableLiveData<String?>()
    val dischargeDateTime: LiveData<String?>
        get() = _dischargeDateTime

    val dischargeString = discharges.toString()
//    val dischargeString = Transformations.map(discharges) { discharges ->
//        formatDischarges(discharges, application.resources)
//    }

    init {
        _dischargeDateTime.value = getCurrentDateTime()
    }

    fun initializeDischarge() {
        uiScope.launch {
            val newEntry = DischargeData()
            insertEntry(newEntry)
            recentDischarge.value = database.getRecentDischarge()
        }
    }

    private suspend fun insertEntry(dischargeId: DischargeData) {
        withContext(Dispatchers.IO) {
            database.addNew(dischargeId)
        }
    }

    fun onDischargeType(typeValue: Int) {
        uiScope.launch {
            val typeEntry = recentDischarge.value ?: return@launch
            typeEntry.dischargeType = typeValue
        }
    }
//    private suspend fun getRecentDischargeFromDatabase(): DischargeData? {
//        return withContext(Dispatchers.IO) {
//            var entry = database.getRecentDischarge()
//            if (entry?.endTimeMilli != entry?.startTimeMilli) {
//                entry = null
//            }
//            entry
//        }
//    }

    fun getCurrentDateTime(): String? {
        // Get the current time (in millis)
        val now = Date().time

        // Create a formatter along with the desired output pattern
        val formatter = SimpleDateFormat("EEEE,  MMMM dd ''yy\n hh:mm a", Locale.getDefault())

        // Put the time (in millis) in our formatter
        val result = formatter.format(now)

        return result
    }
}
