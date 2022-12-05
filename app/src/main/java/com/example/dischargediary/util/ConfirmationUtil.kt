package com.example.dischargediary.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


fun showToast(str: String?, context: Context) {
    Toast.makeText(context, str, Toast.LENGTH_LONG).show()
}

fun snackBarEvent(view: View, str: CharSequence) {
    Snackbar.make(view, str, Snackbar.LENGTH_SHORT).show()
}