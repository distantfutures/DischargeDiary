package com.example.dischargediary

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class DateTime {
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
        val dateTimeInstance = Calendar.getInstance()
        startYear = dateTimeInstance.get(Calendar.YEAR)
        startMonth = dateTimeInstance.get(Calendar.MONTH)
        startDay = dateTimeInstance.get(Calendar.DAY_OF_MONTH)
        startHour = dateTimeInstance.get(Calendar.HOUR_OF_DAY)
        startMinute = dateTimeInstance.get(Calendar.MINUTE)
        dateTimeInstance.set(startYear, startMonth, startDay, startHour, startMinute)
        currentDateTime = dateTimeInstance
        return Pair(formatterDate.format(currentDateTime.time), formatterTime.format(currentDateTime.time))
    }


    // Gets New DateTime from Fragment DateTimePickerDialog
    @RequiresApi(Build.VERSION_CODES.O)
    fun pickADateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): Pair<String, String> {
        val pickDateTime = Calendar.getInstance()
        pickDateTime.set(year, month, day, hour, minute)
        pickedDateTime = pickDateTime
        return Pair(formatterDate.format(pickDateTime.time), formatterTime.format(pickDateTime.time))
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