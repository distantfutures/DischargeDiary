package com.example.dischargediary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DischargeData::class], version = 6, exportSchema = false)
abstract class DischargeDatabase : RoomDatabase() {

    abstract val dischargeDatabaseDao: DischargeDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: DischargeDatabase? = null

        fun getInstance(context: Context): DischargeDatabase {

            synchronized(this) {
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DischargeDatabase::class.java,
                        "discharge_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}