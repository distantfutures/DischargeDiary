package com.example.dischargediary.dischargediary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.databinding.DischargeGridViewBinding

class DischargeGridAdapter() : ListAdapter<DischargeData, DischargeGridAdapter.DischargeViewHolder>(DiffCallback) {
    class DischargeViewHolder(private var binding: DischargeGridViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (data: DischargeData) {
            binding.dischargeData = data
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DischargeData>() {
        override fun areItemsTheSame(oldItem: DischargeData, newItem: DischargeData): Boolean {
            return oldItem.entryId == newItem.entryId
        }

        override fun areContentsTheSame(oldItem: DischargeData, newItem: DischargeData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DischargeViewHolder {
        return DischargeViewHolder(DischargeGridViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DischargeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class DischargeEntryListener(val clickListener: (entryId: Int) -> Unit){
    fun onClick(entry: DischargeData) = clickListener(entry.entryId)
}