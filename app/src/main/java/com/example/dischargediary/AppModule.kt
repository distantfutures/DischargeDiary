package com.example.dischargediary

import com.example.dischargediary.data.DischargeDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

// Create an Application Module
// Lives as long as the application
@Module
@InstallIn(Singleton::class)
object AppModule {

    @Singleton
    @Provides
    fun dataInject(database: DischargeDatabaseDao) = database
}