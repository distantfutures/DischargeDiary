package com.example.dischargediary.workers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dischargediary.createNotificationChannel
import com.example.dischargediary.data.DischargeDatabase
import java.io.FileOutputStream

private const val TAG = "CheckWorker"
class ExportDbWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val FILE_NAME = "test.txt"
    val dischargeDB = DischargeDatabase.getInstance(context)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        val appContext = applicationContext
        val dischargeDiary = dischargeDB.dischargeDatabaseDao.getDischargesList()
        val dataSize = dischargeDiary?.size
        Log.i(TAG, "Work Started!")
        return try {
            Log.i(TAG, "Trying")
            var file: FileOutputStream? = null
            file = appContext.openFileOutput(FILE_NAME, MODE_PRIVATE)
            for (i in 0 until dataSize!!) {
                val entry = dischargeDiary[i].toString() + '\n'
                file.write(entry.toByteArray())
                Log.i(TAG, "Entry $i: $entry")
            }
            Log.i(TAG, "Size: ${dischargeDiary.size}")
            createNotificationChannel(appContext, "Diary Exported!")
            Result.success()
        } catch (e: Throwable) {
            e.printStackTrace()
            Log.i(TAG, "Worker Failed!")
            Result.failure()
        }
    }
}