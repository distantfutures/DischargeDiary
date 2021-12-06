package com.example.dischargediary.dischargediary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeDiaryBinding

class DischargeDiaryFragment : Fragment() {

    private lateinit var viewModel: DischargeDiaryViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDischargeDiaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discharge_diary, container, false)
        viewModel = ViewModelProvider(this).get(DischargeDiaryViewModel::class.java)
        binding.dischargeDiaryViewModel = viewModel
        binding.lifecycleOwner = this

        // val application = requireNotNull(this.activity).application
        binding.numberOneButton.setOnClickListener() { view: View ->
            view.findNavController().navigate(R.id.action_discharge_diary_fragment_to_discharge_fragment)
        }
        binding.numberTwoButton.setOnClickListener() { view: View ->
            view.findNavController().navigate(R.id.action_discharge_diary_fragment_to_discharge_fragment)
        }
        return binding.root
    }
}
