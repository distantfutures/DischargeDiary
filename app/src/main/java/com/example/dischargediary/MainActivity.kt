package com.example.dischargediary

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        scheduleNotification()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Discharge Diary Reminder!"
        val message = "Have you gone today? Log it!"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_DAY
        val intervalTime = AlarmManager.INTERVAL_HOUR + AlarmManager.INTERVAL_HOUR
        alarmManager.setInexactRepeating(
            // Elapsed Realtime Wakeup is more scalable due to locale changes
            // or system time changes. vs RTC Wakeup
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            intervalTime,
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val name = "Notification Channel"
        val description = "A Description of the Channel in the Settings"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)
        channel.description = description
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}