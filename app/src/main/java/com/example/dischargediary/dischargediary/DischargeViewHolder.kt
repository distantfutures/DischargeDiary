package com.example.dischargediary.dischargediary

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dischargediary.R
import com.example.dischargediary.application.AlertDialog
import com.example.dischargediary.application.formatDischargesDialog
import com.example.dischargediary.application.stringToImageRef1
import com.example.dischargediary.application.stringToImageRef2
import com.example.dischargediary.data.DischargeData
import com.example.dischargediary.databinding.DischargeGridViewBinding
import timber.log.Timber

// Holds views from GridView layout
class DischargeViewHolder(var binding: DischargeGridViewBinding) : RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.Q)
    fun bind (data: DischargeData, clickListener: DischargeEntryListener, fragManager: FragmentManager) {
        val ctx = binding.root.context
        binding.dischargeData = data
        binding.clickListener = clickListener
        val border = AppCompatResources.getDrawable(ctx, R.drawable.border_color)
        val color = stringToImageRef1(data.dischargeColor, ctx.resources)
        border?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(ctx, color), PorterDuff.Mode.SRC_IN)
        binding.infoText.background = border
        binding.executePendingBindings()

        // Sets clickListener for item
        binding.dischargeEntry.setOnClickListener(View.OnClickListener {
            showDialog(data, fragManager)
            Timber.d(data.dischargeTime + " clicked! Position: ")
        })
    }

    private fun showDialog(disMilliId: DischargeData?, fragManager: FragmentManager) {
        val setIcon = stringToImageRef2(disMilliId?.dischargeConsistency!!, binding.root.context.resources)
        val icon = ResourcesCompat.getDrawable(binding.root.context.resources, setIcon, null)
        val entryInfo = formatDischargesDialog(disMilliId, binding.root.context.resources).toString()
        val dialog = AlertDialog("Discharge Entry", entryInfo, icon!!)
        dialog.show(fragManager, "Entry Dialog")
        Timber.d("Long clicked! Icon: $icon!!")
    }
}
