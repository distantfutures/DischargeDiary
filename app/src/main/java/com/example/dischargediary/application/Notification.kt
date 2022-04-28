package com.example.dischargediary.application

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dischargediary.R

const val notificationId = 1
const val channelId = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_delete)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(context: Context, message: String) {
    val name = "Notification Channel"
    val description = "A Description of the Channel in the Settings"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(channelId, name, importance)
    channel.description = description
    val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
    Log.i(TAG, "Notification Triggered!")

    // Create the notification
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Discharge Diary")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(1, builder.build())
}

@RequiresApi(Build.VERSION_CODES.O)
fun scheduleNotification(context: Context) {
    val intent = Intent(context, Notification::class.java)
    val title = "Discharge Diary Reminder!"
    val message = "Have you gone today? Log it!"
    intent.putExtra(titleExtra, title)
    intent.putExtra(messageExtra, message)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notificationId,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
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
    createNotificationChannel(context, message)
}