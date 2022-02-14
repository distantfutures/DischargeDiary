package com.example.dischargediary

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.dischargediary.DischargeGridAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recycler: RecyclerView, data: List<DischargeData>?) {
    val adapter = recycler.adapter as DischargeGridAdapter
    adapter.submitList(data)
}
@BindingAdapter("listDataFormatted")
fun TextView.setDataText(disMilliId: DischargeData?) {
    disMilliId?.let {
        text = formatDischarges(disMilliId, context.resources)
    }
}

@BindingAdapter("dischargeGraphic")
fun ImageView.setDischargeGraphic(disMilliId: DischargeData?) {
    disMilliId?.let {
        setBackgroundResource(when (disMilliId.dischargeColor) {
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
        })
        setImageResource(when (disMilliId.dischargeConsistency) {
            R.string.consist_one.toString() -> R.drawable.ic_stool_consistency_01
            R.string.consist_two.toString() -> R.drawable.ic_stool_consistency_02
            R.string.consist_three.toString() -> R.drawable.ic_stool_consistency_04
            R.string.consist_four.toString() -> R.drawable.ic_stool_consistency_05
            R.string.consist_five.toString() -> R.drawable.ic_stool_consistency_06
            else -> R.drawable.ic_urinate_icon
        })
    }
}