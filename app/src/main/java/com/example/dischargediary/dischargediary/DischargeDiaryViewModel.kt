package com.example.dischargediary.dischargediary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.data.DischargeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DischargeDiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val _dischargeDateTime = MutableLiveData<String?>()
    val dischargeDateTime: LiveData<String?>
        get() = _dischargeDateTime

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//    private var recentDischarge = MutableLiveData<DischargeData?>()
//    private val discharges = database.getAllDischarges()

    //val dischargeString = discharges.toString()
//    val dischargeString = Transformations.map(discharges) { discharges ->
//        formatDischarges(discharges, application.resources)
//    }
    private val getAllEntries: LiveData<List<DischargeData>>
    private val repository: DischargeRepository

    init {
        _dischargeDateTime.value = getCurrentDateTime()
//      initializeDischarge()
        val dischargeDao = DischargeDatabase.getDatabase(application).dischargeDatabaseDao()
        repository = DischargeRepository(dischargeDao)
        getAllEntries = repository.getAllDischargeEntries
    }
    val dischargeString = getAllEntries.toString()

//    fun initializeDischarge() {
//        viewModelScope.launch {
//            recentDischarge.value = getRecentFromDatabase()
//        }
//    }
//
//    private suspend fun getRecentFromDatabase(): DischargeData? {
//        return withContext(Dispatchers.IO) {
//            var entry = database.getRecentDischarge()
//            return@withContext entry
//        }
//    }
//
//    private suspend fun insertEntry(dischargeId: DischargeData) {
//        withContext(Dispatchers.IO) {
//            database.addNew(dischargeId)
//        }
//    }

    fun addNewEntry(discharge: DischargeData) {
        viewModelScope.launch {
//            val newEntry = DischargeData()
//            insertEntry(newEntry)
//            recentDischarge.value = getRecentFromDatabase()
            repository.addNewEntry(discharge)
        }
    }
//    fun onDischargeType(typeValue: Int) {
//        uiScope.launch {
//            val typeEntry = recentDischarge.value ?: return@launch
//            typeEntry.dischargeType = typeValue
//        }
//    }
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
