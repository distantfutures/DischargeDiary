package com.example.dischargediary.dischargediary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeDiaryBinding

class DischargeDiaryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDischargeDiaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discharge_diary, container, false)
        val application = requireNotNull(this.activity).application

        return binding.root
    }
}