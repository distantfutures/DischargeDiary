package com.example.dischargediary.util

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeBinding

class UiSetterUtil {

    // Sets appropriate colors for Discharge Color pending DischargeType selection
    @RequiresApi(Build.VERSION_CODES.M)
    fun showUiOf(disType: Int, binding: FragmentDischargeBinding, context: Context) {
        with(binding) {
            color1Button.setBackgroundColor(context.getColor(colorResRef(disType,1)))
            color2Button.setBackgroundColor(context.getColor(colorResRef(disType,2)))
            color3Button.setBackgroundColor(context.getColor(colorResRef(disType,3)))
            color4Button.setBackgroundColor(context.getColor(colorResRef(disType,4)))
            color5Button.setBackgroundColor(context.getColor(colorResRef(disType,5)))
            if (disType == 1) {
                consistGroup.visibility = View.GONE
                dischargeButtonToggleGroup.check(R.id.numberOneButton)
            } else {
                consistGroup.visibility = View.VISIBLE
                dischargeButtonToggleGroup.check(R.id.numberTwoButton)
            }
        }
    }

    private fun colorResRef(disType: Int, buttonNumber: Int): Int {
        val colorRes: Int
        if (disType == 1) {
            colorRes = when (buttonNumber) {
                1 -> R.color.urine_color_one
                2 -> R.color.urine_color_two
                3 -> R.color.urine_color_three
                4 -> R.color.urine_color_four
                5 -> R.color.urine_color_five
                else -> 0
            }
        } else {
            colorRes = when (buttonNumber) {
                1 -> R.color.stool_color_one
                2 -> R.color.stool_color_two
                3 -> R.color.stool_color_three
                4 -> R.color.stool_color_four
                5 -> R.color.stool_color_five
                else -> 0
            }
        }
        return colorRes
    }

    private fun stringColorRef(disType: Int, buttonNumber: Int): Int {
        val colorRef: Int
        if (disType == 1) {
            colorRef = when (buttonNumber) {
                1 -> R.string.urine_color_one
                2 -> R.string.urine_color_two
                3 -> R.string.urine_color_three
                4 -> R.string.urine_color_four
                5 -> R.string.urine_color_five
                else -> R.string.n_a
            }
        } else {
            colorRef = when (buttonNumber) {
                1 -> R.string.stool_color_one
                2 -> R.string.stool_color_two
                3 -> R.string.stool_color_three
                4 -> R.string.stool_color_four
                5 -> R.string.stool_color_five
                else -> R.string.n_a
            }
        }
        return colorRef
    }

    fun stringConsistRef(consist: Int): Int {
        return when (consist) {
            1 -> R.string.consist_one
            2 -> R.string.consist_two
            3 -> R.string.consist_three
            4 -> R.string.consist_four
            5 -> R.string.consist_five
            else -> R.string.n_a
        }
    }

    // Converts button selection to appropriate color pending dischargeType
    fun mapButtonNumberToColors(group: Int, buttonNumber: Int, context: Context): String {
        // TODO: Change group to enum class
        val uiSet = UiSetterUtil()
        return when (group) {
            1 -> uiSet.stringColorRef(group, buttonNumber).let { context.resources.getString(it) }
            2 -> uiSet.stringColorRef(group, buttonNumber).let { context.resources.getString(it) }
            else -> "NOT AVAILABLE"
        }
    }
}