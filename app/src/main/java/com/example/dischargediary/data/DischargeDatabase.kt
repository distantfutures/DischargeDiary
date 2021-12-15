package com.example.dischargediary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DischargeData::class], version = 1, exportSchema = false)
abstract class DischargeDatabase : RoomDatabase() {

    //abstract val dischargeDatabaseDao: DischargeDatabaseDao
    abstract fun dischargeDatabaseDao(): DischargeDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: DischargeDatabase? = null

        fun getDatabase(context: Context): DischargeDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DischargeDatabase::class.java,
                    "discharge_history_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}