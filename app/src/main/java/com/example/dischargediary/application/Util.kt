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

fun stringToImageRef1(dischargeColor: String) : Int {
    return when (dischargeColor) {
        // urine
        R.string.urine_color_one.toString() -> R.color.urine_color_one
        R.string.urine_color_two.toString() -> R.color.urine_color_two
        R.string.urine_color_three.toString() -> R.color.urine_color_three
        R.string.urine_color_four.toString() -> R.color.urine_color_four
        R.string.urine_color_five.toString() -> R.color.urine_color_five
        // stool
        R.string.stool_color_one.toString() -> R.color.stool_color_one
        R.string.stool_color_two.toString() -> R.color.stool_color_two
        R.string.stool_color_three.toString() -> R.color.stool_color_three
        R.string.stool_color_four.toString() -> R.color.stool_color_four
        R.string.stool_color_five.toString() -> R.color.stool_color_five
        else -> R.color.white
    }
}

fun stringToImageRef2(consist: String): Int {
    return when (consist) {
        R.string.consist_one.toString() -> R.drawable.ic_stool_consistency_01
        R.string.consist_two.toString() -> R.drawable.ic_stool_consistency_02
        R.string.consist_three.toString() -> R.drawable.ic_stool_consistency_04
        R.string.consist_four.toString() -> R.drawable.ic_stool_consistency_05
        R.string.consist_five.toString() -> R.drawable.ic_stool_consistency_06
        else -> R.drawable.ic_urinate_icon
    }
}