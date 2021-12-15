package com.example.dischargediary.dischargediary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dischargediary.R
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.databinding.FragmentDischargeDiaryBinding

class DischargeDiaryFragment : Fragment() {

    //private lateinit var viewModel: DischargeDiaryViewModel
    //private val sharedViewModel: DischargeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDischargeDiaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discharge_diary, container, false)

        val application = requireNotNull(this.activity).application

        val datasource = DischargeDatabase.getInstance(application).dischargeDatabaseDao

        val viewModelFactory = DischargeDiaryViewModelFactory(datasource, application)

        val diaryViewModel = ViewModelProvider(this, viewModelFactory).get(DischargeDiaryViewModel::class.java)

        binding.lifecycleOwner = this

        binding.dischargeDiaryViewModel = diaryViewModel

//        binding.numberOneButton.setOnClickListener() { view: View ->
//            view.findNavController().navigate(R.id.action_discharge_diary_fragment_to_discharge_fragment)
//            //viewModel.onNewEntry()
//            //viewModel.onDischargeType(1)
//        }
//        binding.numberTwoButton.setOnClickListener() { view: View ->
//            view.findNavController().navigate(R.id.action_discharge_diary_fragment_to_discharge_fragment)
//        }
        //something about this breaks the viewmodelfactory
        diaryViewModel.navigateToDischargeEntry.observe(viewLifecycleOwner, Observer { entry ->
            entry?.let {
                this.findNavController().navigate(
                    DischargeDiaryFragmentDirections.actionDischargeDiaryFragmentToDischargeFragment(entry.entryId))
                    Log.d("DiaryFrag", "Navigate, ${entry.entryId}")
                diaryViewModel.doneNavigating()
            }
        })
        return binding.root
    }
}
