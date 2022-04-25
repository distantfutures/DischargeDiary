package com.example.dischargediary.dischargediary

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.repository.DischargesRepository
import com.example.dischargediary.workers.ExportDbWorker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "CheckDDVM"
const val TAG_OUTPUT = "OUTPUT"
const val EXPORT_WORK_NAME = "export_work"
class DischargeDiaryViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    internal val outputWorkInfos: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    private val dischargesRepository = DischargesRepository(DischargeDatabase.getInstance(application))
    val dischargeDiary = dischargesRepository.allDischarges

    private val _dischargeDateTime = MutableLiveData<String?>()
    val dischargeDateTime: LiveData<String?>
        get() = _dischargeDateTime

    private val _dischargeTypeArg = MutableLiveData<Int>()
    val dischargeTypeArg: LiveData<Int>
        get() = _dischargeTypeArg

    private val _diaryExported = MutableLiveData<Boolean>()
    val diaryExported: LiveData<Boolean>
        get() = _diaryExported

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
            Log.d(TAG, "Delete Entry! $disMilliId")
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

    fun exportFile() {
        val exportWork = workManager
            .beginUniqueWork(
                EXPORT_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(ExportDbWorker::class.java)
            )
        exportWork.enqueue()

        Log.i(TAG, "Export Clicked!")
    }
}