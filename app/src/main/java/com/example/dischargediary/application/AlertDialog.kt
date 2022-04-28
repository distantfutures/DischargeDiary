package com.example.dischargediary.application

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AlertDialog(title: String, message: String, icon: Drawable): DialogFragment() {
    var titleText = title
    var messageText = message
    var iconSet = icon
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setIcon(iconSet)
                .setTitle(titleText)
                .setMessage(messageText)
                .setNegativeButton("Close") { _, _ ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}