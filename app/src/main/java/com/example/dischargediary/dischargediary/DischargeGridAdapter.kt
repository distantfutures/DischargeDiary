package com.example.dischargediary.dischargediary

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.databinding.DischargeGridViewBinding

class DischargeGridAdapter(private val clickListener: DischargeEntryListener) : ListAdapter<DischargeData, DischargeViewHolder>(DiffCallback) {

    private var currentSelectedPosition: Int = RecyclerView.NO_POSITION

    companion object DiffCallback : DiffUtil.ItemCallback<DischargeData>() {
        override fun areItemsTheSame(oldItem: DischargeData, newItem: DischargeData): Boolean {
            return oldItem.dischargeMilli == newItem.dischargeMilli
        }

        override fun areContentsTheSame(oldItem: DischargeData, newItem: DischargeData): Boolean {
            return oldItem == newItem
        }
    }

    // Updates after Item is deleted from Room
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCurrentListChanged(
        previousList: MutableList<DischargeData>,
        currentList: MutableList<DischargeData>
    ) {
        notifyDataSetChanged()
        currentSelectedPosition = RecyclerView.NO_POSITION
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DischargeViewHolder {
        return DischargeViewHolder(DischargeGridViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    // Binds ViewHolder & Position/Data in View
    override fun onBindViewHolder(holder: DischargeViewHolder, position: Int) {
        val item = getItem(position)
        // Calls bind function from ViewHolder
        holder.bind(item, clickListener)
        holder.itemView.setOnClickListener(View.OnClickListener {
            currentSelectedPosition = position
            notifyDataSetChanged()
            Log.i("CheckAdapter", "${item.dischargeTime} clicked! Position: $position")
        })
        if (currentSelectedPosition == position) {
            holder.binding.deleteButton.visibility = View.VISIBLE
        } else {
            holder.binding.deleteButton.visibility = View.GONE
        }
    }
}
//1 - get data from clicklistener
class DischargeEntryListener(val clickListener: (disMilli: Long) -> Unit){
    fun onClick(entry: DischargeData) {
        clickListener(entry.dischargeMilli)
    }
}