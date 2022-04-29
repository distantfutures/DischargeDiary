package com.example.dischargediary.dischargediary

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.R
import com.example.dischargediary.application.AlertDialog
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.databinding.DischargeGridViewBinding

const val ADAPTER_TAG = "CheckAdapter"
class DischargeGridAdapter(private val context: Context, private val fragment: FragmentManager, private val clickListener: DischargeEntryListener) : ListAdapter<DischargeData, DischargeViewHolder>(DiffCallback) {

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
        currentSelectedPosition = RecyclerView.NO_POSITION
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
        holder.bind(item, clickListener)
        // Sets clickListener for item
        holder.itemView.setOnClickListener(View.OnClickListener {
            currentSelectedPosition = position
            notifyDataSetChanged()
            Log.i(ADAPTER_TAG, "${item.dischargeTime} clicked! Position: $position")
        })
        // Sets LongClickListener for item
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            longSelectedPosition = position
            notifyDataSetChanged()
            Log.i(ADAPTER_TAG, "${item.dischargeTime} Long clicked! Position: $position")
            return@OnLongClickListener true
        })
        // Shows and hides delete button per item
        if (currentSelectedPosition == position) {
            holder.binding.deleteButton.visibility = View.VISIBLE
        } else {
            holder.binding.deleteButton.visibility = View.GONE
        }
        // Opens basic AlertDialog box. To implement more Entry information later
        if (longSelectedPosition == position) {
            showDialog()
        }
    }
    fun showDialog() {
        val icon = ResourcesCompat.getDrawable(context.resources!!,R.drawable.ic_urinate_icon, null)
        val dialog = AlertDialog("title1", "This is my Message",icon!!)
        dialog.show(fragment, "Entry Dialog")
        Log.i(ADAPTER_TAG, "Long clicked! Icon: $icon!!")
    }
}

class DischargeEntryListener(val clickListener: (disMilli: Long) -> Unit){
    fun onClick(entry: DischargeData) {
        clickListener(entry.dischargeMilli)
    }
}