package com.example.dischargediary.application

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.dischargediary.R
import com.example.dischargediary.data.DischargeData

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

fun formatDischargeTime(discharges: DischargeData): Spanned? {
    val sb = StringBuilder()
    sb.apply {
        discharges.let {
            append("<b>${it.dischargeDate}</b> <br>${it.dischargeTime}")
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun formatDischargesDialog(discharges: DischargeData, resources: Resources): Spanned? {
    val sb = StringBuilder()
    sb.apply {
        discharges.let {
            append("<br><b>${resources.getString(R.string.discharge_type)}</b> ")
            append("#${it.dischargeType}")
            append("<br><b>${resources.getString(R.string.date)}</b> ")
            append(it.dischargeDate)
            append("<br><b>${resources.getString(R.string.time)}</b> ")
            append(it.dischargeTime)
            append("<br><b>${resources.getString(R.string.duration)}</b> ")
            append(it.dischargeDuration)
            append("<br><b>${resources.getString(R.string.leakage)}</b> ")
            if (it.leakage) append(resources.getString(R.string.yes)) else append(resources.getString(R.string.no))
            append("<br><b>${resources.getString(R.string.color)}</b> ")
            append(it.dischargeColor)
            if (it.dischargeType == 2) {
                append("<br><b>${resources.getString(R.string.consistency)}</b> ")
                append(it.dischargeConsistency)
            }
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun stringToImageRef1(dischargeColor: String, res: Resources) : Int {
    return when (dischargeColor) {
        // urine
        res.getString(R.string.urine_color_one) -> R.color.urine_color_one
        res.getString(R.string.urine_color_two) -> R.color.urine_color_two
        res.getString(R.string.urine_color_three) -> R.color.urine_color_three
        res.getString(R.string.urine_color_four) -> R.color.urine_color_four
        res.getString(R.string.urine_color_five) -> R.color.urine_color_five
        // stool
        res.getString(R.string.stool_color_one) -> R.color.stool_color_one
        res.getString(R.string.stool_color_two) -> R.color.stool_color_two
        res.getString(R.string.stool_color_three) -> R.color.stool_color_three
        res.getString(R.string.stool_color_four) -> R.color.stool_color_four
        res.getString(R.string.stool_color_five) -> R.color.stool_color_five
        else -> R.color.white
    }
}

fun stringToImageRef2(consist: String, res: Resources): Int {
    return when (consist) {
        res.getString(R.string.consist_one) -> R.drawable.ic_stool_consistency_01
        res.getString(R.string.consist_two) -> R.drawable.ic_stool_consistency_02
        res.getString(R.string.consist_three) -> R.drawable.ic_stool_consistency_04
        res.getString(R.string.consist_four) -> R.drawable.ic_stool_consistency_05
        res.getString(R.string.consist_five) -> R.drawable.ic_stool_consistency_06
        else -> R.drawable.ic_urinate_icon
    }
}