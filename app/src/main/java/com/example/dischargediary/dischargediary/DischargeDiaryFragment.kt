package com.example.dischargediary.dischargediary

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeDiaryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DischargeDiaryFragment : Fragment() {

    private val diaryViewModel: DischargeDiaryViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDischargeDiaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discharge_diary, container, false)

        binding.lifecycleOwner = this
        binding.dischargeDiaryViewModel = diaryViewModel

        // Sets clicklistener with entry reference, then binds to RecyclerView
        val adapter = DischargeGridAdapter(activity?.application!!,parentFragmentManager, DischargeEntryListener { disMilli ->
            diaryViewModel.deleteEntryFromRepository(disMilli)
        })
        binding.dischargeList.adapter = adapter

        //Passes the entryId argument through actions to DischargeEntry
        diaryViewModel.dischargeTypeArg.observe(viewLifecycleOwner) { newEntry ->
            if (newEntry != 0) {
                this.findNavController().navigate(
                    DischargeDiaryFragmentDirections.actionDischargeDiaryFragmentToDischargeFragment(
                        diaryViewModel.dischargeTypeArg.value!!
                    )
                )
                diaryViewModel.doneNavigating()
            }
        }
        return binding.root
    }
}
