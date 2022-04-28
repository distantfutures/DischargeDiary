package com.example.dischargediary.application

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.dischargediary.R
import com.example.dischargediary.data.DischargeData

const val TAG = "Util"
fun formatDischarges(discharges: DischargeData, resources: Resources): Spanned? {
    val sb = StringBuilder()
    sb.apply {
        discharges.let {
            append("<br><b>${resources.getString(R.string.date)}</b> ")
            append(it.dischargeDate)
            append("<br><b>${resources.getString(R.string.time)}</b> ")
            append(it.dischargeTime)
            append("<br><b>${resources.getString(R.string.discharge_type)}</b> ")
            append("#${it.dischargeType}")
            append("<br><b>${resources.getString(R.string.duration)}</b> ")
            append(it.dischargeDuration)
            append("<br><b>${resources.getString(R.string.leakage)}</b> ")
            if (it.leakage) {
                append(resources.getString(R.string.yes))
            } else {
                append(resources.getString(R.string.no))
            }
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}