package com.example.dischargediary.dischargediary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.data.DischargeDatabaseDao
import java.text.SimpleDateFormat
import java.util.*

class DischargeDiaryViewModel(
    val database: DischargeDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _dischargeDateTime = MutableLiveData<String?>()
    val dischargeDateTime: LiveData<String?>
        get() = _dischargeDateTime

    private var recentDischarge = MutableLiveData<DischargeData?>()

    private val discharges = database.getAllDischarges()

    val dischargeString = discharges.toString()

//    val dischargeString = Transformations.map(discharges) { discharges ->
//        formatDischarges(discharges, application.resources)
//    }

    init {
        _dischargeDateTime.value = getCurrentDateTime()
    }

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
