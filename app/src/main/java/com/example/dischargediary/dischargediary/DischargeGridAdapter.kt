package com.example.dischargediary.dischargediary

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.databinding.DischargeGridViewBinding

class DischargeGridAdapter(private val clickListener: DischargeEntryListener) : ListAdapter<DischargeData, DischargeGridAdapter.DischargeViewHolder>(DiffCallback) {

    private var currentSelectedPosition: Int = RecyclerView.NO_POSITION
    // Holds views from GridView layout
    class DischargeViewHolder(var binding: DischargeGridViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (data: DischargeData, clickListener: DischargeEntryListener) {
            binding.dischargeData = data
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DischargeData>() {
        override fun areItemsTheSame(oldItem: DischargeData, newItem: DischargeData): Boolean {
            return oldItem.dischargeMilli == newItem.dischargeMilli
        }

        override fun areContentsTheSame(oldItem: DischargeData, newItem: DischargeData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DischargeViewHolder {
        return DischargeViewHolder(DischargeGridViewBinding.inflate(LayoutInflater.from(parent.context)))
    }
    // Binds ViewHolder & Position/Data in View
    override fun onBindViewHolder(holder: DischargeViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener(View.OnClickListener {
            currentSelectedPosition = position
            notifyDataSetChanged()
        })
        if (currentSelectedPosition == position) {
            holder.binding.deleteButton.visibility = View.VISIBLE
        } else {
            holder.binding.deleteButton.visibility = View.GONE
        }
        // Calls bind function from ViewHolder
        holder.bind(item, clickListener)
    }
}
//1 - get data from clicklistener
class DischargeEntryListener(val clickListener: (disMilli: Long) -> Unit){
    fun onClick(entry: DischargeData) {
        clickListener(entry.dischargeMilli)
        Log.i("CheckAdapter", "$entry clicked!")
    }
}