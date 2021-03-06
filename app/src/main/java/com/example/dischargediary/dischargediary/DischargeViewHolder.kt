package com.example.dischargediary.dischargediary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.databinding.DischargeGridViewBinding

// Holds views from GridView layout
class DischargeViewHolder(var binding: DischargeGridViewBinding) : RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.Q)
    fun bind (data: DischargeData, clickListener: DischargeEntryListener) {
        binding.dischargeData = data
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}
