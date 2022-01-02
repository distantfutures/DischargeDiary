package com.example.dischargediary

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.dischargediary.data.DischargeData

fun formatDischarges(discharges: List<DischargeData>, resources: Resources): Spanned? {
    val sb = StringBuilder()
    sb.apply {
        discharges.forEach {
            append("<br>Date: ${it.dischargeDate}")
            append("<br>Time: ${it.dischargeTime}")
            append("<br>Type: ${it.dischargeType}")
            append("<br>Duration: ${it.dischargeDuration}")
            append("<br>Leakage: ${it.leakage}")
            append("<br>Color: ${it.dischargeColor}")
            append("<br>Consistency: ${it.dischargeConsistency}")
            append("<br>")
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
