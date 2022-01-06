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
            append("<br><b>${resources.getString(R.string.date)}</b> ")
            append("${it.dischargeDate}")
            append("<br><b>${resources.getString(R.string.time)}</b> ")
            append("${it.dischargeTime}")
            append("<br><b>${resources.getString(R.string.discharge_type)}</b> ")
            append("#${it.dischargeType}")
            append("<br><b>${resources.getString(R.string.duration)}</b> ")
            append("${it.dischargeDuration}")
            append("<br><b>${resources.getString(R.string.leakage)}</b> ")
            if (it.leakage == true) {
                append(resources.getString(R.string.yes))
            } else {
                append(resources.getString(R.string.no))
            }
            append("<br><b>${resources.getString(R.string.color)}</b> ")
            append("${it.dischargeColor}")
            append("<br><b>${resources.getString(R.string.consistency)}</b> ")
            append("${it.dischargeConsistency}")
            append("<br>")
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
