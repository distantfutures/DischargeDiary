package com.example.dischargediary.application

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.dischargediary.DischargeGridAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val adapterScope = CoroutineScope(Dispatchers.Default)

@BindingAdapter("listData")
fun bindRecyclerView(recycler: RecyclerView, data: List<DischargeData>?) {
    adapterScope.launch {
        val adapter = recycler.adapter as DischargeGridAdapter
        withContext(Dispatchers.Main) {
            adapter.submitList(data)
        }
    }
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
        setBackgroundResource(stringToImageRef1(disMilliId.dischargeColor))
        setImageResource(stringToImageRef2(disMilliId.dischargeConsistency))
    }
}