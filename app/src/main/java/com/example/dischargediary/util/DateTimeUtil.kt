package com.example.dischargediary.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeUtil {
    private val calInstance: Calendar = Calendar.getInstance()
    var startYear = 0
    var startMonth = 0
    var startDay = 0
    var startHour = 0
    var startMinute = 0
    private val formatterDate = SimpleDateFormat("MM.dd.yyyy, EEE", Locale.getDefault())
    private val formatterTime = SimpleDateFormat("h:mm a", Locale.getDefault())
    lateinit var currentDateTime: Calendar
    lateinit var pickedDateTime: Calendar

    // Gets initial DateTime from Fragment DateTimePickerDialog
    fun currentDateTimeInstance(): Pair<String, String> {
        startYear = calInstance.get(Calendar.YEAR)
        startMonth = calInstance.get(Calendar.MONTH)
        startDay = calInstance.get(Calendar.DAY_OF_MONTH)
        startHour = calInstance.get(Calendar.HOUR_OF_DAY)
        startMinute = calInstance.get(Calendar.MINUTE)
        calInstance.set(startYear, startMonth, startDay, startHour, startMinute)
        currentDateTime = calInstance
        return Pair(formatterDate.format(currentDateTime.time), formatterTime.format(currentDateTime.time))
    }


    // Gets New DateTime from Fragment DateTimePickerDialog
    @RequiresApi(Build.VERSION_CODES.O)
    fun pickADateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): Pair<String, String> {
        calInstance.set(year, month, day, hour, minute)
        pickedDateTime = calInstance
        return Pair(formatterDate.format(calInstance.time), formatterTime.format(calInstance.time))
    }

    // Sets newly formatted date to LiveData
    @RequiresApi(Build.VERSION_CODES.O)
    fun milliDateTimeFormatter(dateTime: Calendar): Long {
        val simpleFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val simpleFormat = simpleFormatter.format(dateTime.time).toString()
        val localDate: LocalDateTime = LocalDateTime.parse(simpleFormat, formatter)
        return localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}