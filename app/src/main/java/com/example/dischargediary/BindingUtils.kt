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
fun TextView.setDataText(entry: DischargeData?) {
    entry?.let {
        text = formatDischarges(entry, context.resources)
    }
}

@BindingAdapter("dischargeGraphic")
fun ImageView.setDischargeGraphic(entry: DischargeData?) {
    entry?.let {
        setBackgroundResource(when (entry.dischargeColor) {
            "Clear" -> R.color.clear_urine
            "Light Yellow" -> R.color.light_yellow_urine
            "Yellow" -> R.color.yellow_urine
            "Dark Yellow" -> R.color.dark_yellow_urine
            "Red" -> R.color.red_urine
            "Light Grey" -> R.color.light_grey
            "Brown" -> R.color.brown_stool
            "Dark Brown/Black" -> R.color.dark_brown_black_stool
            "Green" -> R.color.green_stool
            "Red Stool" -> R.color.red_stool
            else -> R.color.clear_urine
        })
        setImageResource(when (entry.dischargeConsistency) {
            "Severe Constipation" -> R.drawable.ic_stool_consistency_01
            "Mild Constipation" -> R.drawable.ic_stool_consistency_02
            "Normal" -> R.drawable.ic_stool_consistency_04
            "Lacking Fiber" -> R.drawable.ic_stool_consistency_05
            "Diarrhea" -> R.drawable.ic_stool_consistency_06
            else -> R.drawable.ic_urinate_icon
        })
    }
}