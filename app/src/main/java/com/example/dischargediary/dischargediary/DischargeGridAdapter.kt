package com.example.dischargediary.dischargediary

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.databinding.DischargeGridViewBinding
import timber.log.Timber

class DischargeGridAdapter(
    private val fragment: FragmentManager,
    private val clickListener: DischargeEntryListener
) : ListAdapter<DischargeData, DischargeViewHolder>(DiffCallback) {

    // Starts RV positions at 0
    private var currentSelectedPosition: Int = RecyclerView.NO_POSITION
    private var longSelectedPosition: Int = RecyclerView.NO_POSITION

    companion object DiffCallback : DiffUtil.ItemCallback<DischargeData>() {
        override fun areItemsTheSame(oldItem: DischargeData, newItem: DischargeData): Boolean {
            return oldItem.dischargeMilli == newItem.dischargeMilli
        }

        override fun areContentsTheSame(oldItem: DischargeData, newItem: DischargeData): Boolean {
            return oldItem == newItem
        }
    }

    // Updates after Item is deleted from Room by reseting selected position
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCurrentListChanged(
        previousList: MutableList<DischargeData>,
        currentList: MutableList<DischargeData>
    ) {
        notifyDataSetChanged()
        longSelectedPosition = RecyclerView.NO_POSITION
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DischargeViewHolder {
        return DischargeViewHolder(DischargeGridViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    // Binds ViewHolder & Position/Data in View
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: DischargeViewHolder, position: Int) {
        val item = getItem(position)
        // Calls bind function from ViewHolder
        holder.bind(item, clickListener, fragment)
        // Sets LongClickListener for item
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            longSelectedPosition = position
            notifyDataSetChanged()
            Timber.i(item.dischargeTime + " Long clicked! Position: " + position)
            return@OnLongClickListener true
        })
        // Shows and hides delete button per item
        if (longSelectedPosition == position) {
            holder.binding.deleteButton.visibility = View.VISIBLE
        } else {
            holder.binding.deleteButton.visibility = View.GONE
        }
    }
}

class DischargeEntryListener(val clickListener: (disMilli: Long) -> Unit){
    fun onClick(entry: DischargeData) {
        clickListener(entry.dischargeMilli)
    }
}