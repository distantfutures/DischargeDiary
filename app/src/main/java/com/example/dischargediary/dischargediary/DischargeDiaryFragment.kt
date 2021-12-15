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
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.databinding.FragmentDischargeDiaryBinding

class DischargeDiaryFragment : Fragment() {

    private lateinit var viewModel: DischargeDiaryViewModel
    //private val sharedViewModel: DischargeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDischargeDiaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discharge_diary, container, false)

        val application = requireNotNull(this.activity).application

        val datasource = DischargeDatabase.getInstance(application).dischargeDatabaseDao()

        val viewModelFactory = DischargeDiaryViewModelFactory(datasource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DischargeDiaryViewModel::class.java)

        binding.lifecycleOwner = this

        binding.dischargeDiaryViewModel = viewModel

//        sharedViewModel.dischargeDate.observe(viewLifecycleOwner, { date ->
//            binding.dischargeList.setText(date)
//        })

        // val application = requireNotNull(this.activity).application
        binding.numberOneButton.setOnClickListener() { view: View ->
            view.findNavController().navigate(R.id.action_discharge_diary_fragment_to_discharge_fragment)
            viewModel.initializeDischarge()
            viewModel.onDischargeType(1)
        }
        binding.numberTwoButton.setOnClickListener() { view: View ->
            view.findNavController().navigate(R.id.action_discharge_diary_fragment_to_discharge_fragment)
        }
        return binding.root
    }
}
