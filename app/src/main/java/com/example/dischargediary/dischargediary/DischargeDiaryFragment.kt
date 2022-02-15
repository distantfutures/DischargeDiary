package com.example.dischargediary.dischargediary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

        val adapter = DischargeGridAdapter(DischargeEntryListener { disMilli ->
            diaryViewModel.deleteEntryNumber(disMilli)
        })

        binding.dischargeList.adapter = adapter

        //Passes the entryId argument through actions to DischargeEntry
        diaryViewModel.dischargeTypeArg.observe(viewLifecycleOwner, { newEntry ->
            if (newEntry != 0) {
                this.findNavController().navigate(
                    DischargeDiaryFragmentDirections.actionDischargeDiaryFragmentToDischargeFragment(diaryViewModel.dischargeTypeArg.value!!))
                Log.d("CheckDiaryFrag", "Navigate, ${diaryViewModel.dischargeTypeArg.value!!}")
                diaryViewModel.doneNavigating()
            }
        })
        return binding.root
    }
}
