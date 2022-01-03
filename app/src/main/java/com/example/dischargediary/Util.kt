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
            append("<br><b>Date: </b>${it.dischargeDate}")
            append("<br><b>Time: </b>${it.dischargeTime}")
            append("<br><b>Type: </b> #${it.dischargeType}")
            append("<br><b>Duration: </b>${it.dischargeDuration}")
            append("<br><b>Leakage: </b>${it.leakage}")
            append("<br><b>Color: </b>${it.dischargeColor}")
            append("<br><b>Consistency: </b>${it.dischargeConsistency}")
            append("<br>")
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
