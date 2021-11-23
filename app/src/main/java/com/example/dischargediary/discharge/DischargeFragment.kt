package com.example.dischargediary.discharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeBinding

class DischargeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDischargeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discharge, container, false)
        binding.submitButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_discharge_fragment_to_discharge_diary_fragment)
        }
        return binding.root
    }
}
