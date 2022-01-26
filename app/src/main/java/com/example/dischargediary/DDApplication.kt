package com.example.dischargediary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Create Application Class
// Hilt uses this to create classes behind the scenes
@HiltAndroidApp
class DDApplication : Application()