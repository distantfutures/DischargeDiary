package com.example.dischargediary.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.text.HtmlCompat
import com.example.dischargediary.R
import com.example.dischargediary.data.DischargeData

const val TAG = "Util"
fun formatDischarges(discharges: DischargeData, resources: Resources): Spanned? {
    val sb = StringBuilder()
    sb.apply {
        discharges.let {
            append("<br><b>${resources.getString(R.string.date)}</b> ")
            append(it.dischargeDate)
            append("<br><b>${resources.getString(R.string.time)}</b> ")
            append(it.dischargeTime)
            append("<br><b>${resources.getString(R.string.discharge_type)}</b> ")
            append("#${it.dischargeType}")
            append("<br><b>${resources.getString(R.string.duration)}</b> ")
            append(it.dischargeDuration)
            append("<br><b>${resources.getString(R.string.leakage)}</b> ")
            if (it.leakage) {
                append(resources.getString(R.string.yes))
            } else {
                append(resources.getString(R.string.no))
            }
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
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