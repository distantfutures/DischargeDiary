package com.example.dischargediary.discharge

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeBinding

class DischargeFragment : Fragment() {

    private lateinit var dischargeViewModel: DischargeViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDischargeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_discharge, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = DischargeFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = DischargeViewModelFactory(arguments.dischargeTypeArg, application)
        Log.d("CheckDischargeFrag", "${arguments.dischargeTypeArg}")
        dischargeViewModel = ViewModelProvider(this, viewModelFactory).get(DischargeViewModel::class.java)

        binding.dischargeViewModel = dischargeViewModel
        binding.lifecycleOwner = this

        //Current Date & Time
        binding.dischargeDateTime.setOnClickListener { setNewDateTime() }
        //Observes Discharge Type & sets discharge colors accordingly
        dischargeViewModel.dischargeType.observe(
            viewLifecycleOwner
        ) { number ->
            if (number != 2) {
                showNumberOneUi(binding)
                dischargeViewModel.onSetDischargeColor(dischargeViewModel.dischargeColorButton.value)
                dischargeViewModel.onSetDischargeConsist(0)
            } else {
                showNumberTwoUi(binding)
                dischargeViewModel.onSetDischargeColor(dischargeViewModel.dischargeColorButton.value)
            }
        }
        //Duration/Timer Button
        binding.durationInput.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val time = binding.durationInput.text
                    dischargeViewModel.onSetDischargeDuration(time.toString())
                    false
                }
                else -> { true }
            }
        }
        //Submit Button
        dischargeViewModel.navigateToDiary.observe(viewLifecycleOwner) {
            if (it == true) {
                this.findNavController()
                    .navigate(DischargeFragmentDirections.actionDischargeFragmentToDischargeDiaryFragment())
                dischargeViewModel.doneNavigating()
                showToastLong("Entry Recorded")
            }
            if (it == false) {
                showToastLong("Entry Incomplete")
            }
        }
        return binding.root
    }
    private fun showToast(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }
    private fun showToastLong(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }
    private fun showNumberOneUi(binding: FragmentDischargeBinding) {
        binding.apply {
            consistGroup.visibility = View.GONE
            dischargeButtonToggleGroup.check(R.id.numberOneButton)
            color1Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.urine_color_one))
            color2Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.urine_color_two))
            color3Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.urine_color_three))
            color4Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.urine_color_four))
            color5Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.urine_color_five))
        }
    }
    private fun showNumberTwoUi(binding: FragmentDischargeBinding) {
        binding.apply {
            consistGroup.visibility = View.VISIBLE
            dischargeButtonToggleGroup.check(R.id.numberTwoButton)
            color1Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.stool_color_one))
            color2Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.stool_color_two))
            color3Button.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.stool_color_three))
            color4Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.stool_color_four))
            color5Button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.stool_color_five))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setNewDateTime() {
        DatePickerDialog(requireActivity(), { _, year, month, day ->
            TimePickerDialog(requireActivity(), { _, hour, minute ->
                dischargeViewModel.pickADateTime(year, month, day, hour, minute)
                showToast(dischargeViewModel.dischargeDate.value)
                showToast(dischargeViewModel.dischargeTime.value)
            }, dischargeViewModel.startHour, dischargeViewModel.startMinute, false).show()
        }, dischargeViewModel.startYear, dischargeViewModel.startMonth, dischargeViewModel.startDay).show()
    }
}
