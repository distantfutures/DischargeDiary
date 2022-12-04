package com.example.dischargediary.discharge

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dischargediary.R
import com.example.dischargediary.UiSetter
import com.example.dischargediary.databinding.FragmentDischargeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DischargeFragment : Fragment() {

    private val dischargeViewModel: DischargeViewModel by viewModels()
    private val args: DischargeFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentDischargeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_discharge, container, false)

        val uiSet = UiSetter()

        // Gets arguments from Diary Screen, passes into Factory and sets to ViewModel
        dischargeViewModel.onSetDischargeType(args.dischargeTypeArg)

        binding.dischargeViewModel = dischargeViewModel
        binding.dischargeFragment = this
        binding.lifecycleOwner = this

        // Current Date & Time
        binding.dischargeDateTime.setOnClickListener { setNewDateTime() }

        // Observes Discharge Type & sets UI accordingly
        dischargeViewModel.dischargeType.observe(
            viewLifecycleOwner
        ) { number ->
            if (number != 2) {
                uiSet.showUiOf(1, binding, requireContext())
                dischargeViewModel.onSetDischargeColor(dischargeViewModel.dischargeColorButton)
                dischargeViewModel.onSetDischargeConsist(0)
            } else {
                uiSet.showUiOf(2, binding, requireContext())
                dischargeViewModel.onSetDischargeColor(dischargeViewModel.dischargeColorButton)
            }
        }

        // Submit Button
        dischargeViewModel.navigateToDiary.observe(viewLifecycleOwner) { nav ->
            if (nav == true) {
                this.findNavController()
                    .navigate(DischargeFragmentDirections
                        .actionDischargeFragmentToDischargeDiaryFragment()
                    )
                dischargeViewModel.doneNavigating()
                snackBarEvent("Entry Recorded")
            }
            if (nav == false) context?.resources?.let { it ->
                snackBarEvent(it.getString(R.string.incomplete))
            }
        }

        // Duration input - sets info after keyboard closes
        binding.durationInput.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val time = binding.durationInput.text
                    dischargeViewModel.onSetDischargeDuration(time.toString())
                    false
                }
                else -> true
            }
        }

        return binding.root
    }

    // Opens Date and Time picker dialog
    @RequiresApi(Build.VERSION_CODES.O)
    fun setNewDateTime() {
        val dt = dischargeViewModel.startDateTime.value ?: return
        DatePickerDialog(requireActivity(), { _, year, month, day ->
            TimePickerDialog(requireActivity(), { _, hour, minute ->
                dischargeViewModel.pickNewDateTime(year, month, day, hour, minute)
                showToast(dischargeViewModel.dischargeDate.value)
                showToast(dischargeViewModel.dischargeTime.value)
            }, dt.startHour, dt.startMinute, false).show()
        }, dt.startYear, dt.startMonth, dt.startDay).show()
        Timber.d("XX dateTime: $dt")
    }

    private fun showToast(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }

    private fun snackBarEvent(str: CharSequence) {
        activity?.let { Snackbar.make(it.findViewById(android.R.id.content), str, Snackbar.LENGTH_SHORT).show() }
    }
}
