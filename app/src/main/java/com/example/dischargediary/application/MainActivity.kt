package com.example.dischargediary.application

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.dischargediary.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    /**
     * [Part1] Get ref to Firebase Authentication Object
     **/
    lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // [Part2] Get instance of auth
        auth = FirebaseAuth.getInstance()

        scheduleNotification(this)
    }
}