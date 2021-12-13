package com.example.dischargediary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DischargeData::class], version = 1, exportSchema = false)
abstract class DischargeDatabase : RoomDatabase() {

    abstract val dischargeDatabaseDao: DischargeDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: DischargeDatabase? = null

        fun getInstance(context: Context): DischargeDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DischargeDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}