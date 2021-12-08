package com.example.dischargediary.discharge

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeBinding
import java.util.*

class DischargeFragment : Fragment() {

    private lateinit var viewModel: DischargeViewModel
    //private lateinit var viewModelFactory: DischargeViewModelFactory

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDischargeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discharge, container, false)

        //viewModelFactory = DischargeViewModelFactory(DischargeFragment.fromBundle(arguments!!).dischargeType)
        viewModel = ViewModelProvider(this).get(DischargeViewModel::class.java)
        binding.dischargeViewModel = viewModel
        binding.lifecycleOwner = this

        //Current Date & Time
        binding.dischargeDateTime.setOnClickListener { setNewDateTime() }
        //viewModel.dischargeDateTime.observe(viewLifecycleOwner, androidx.lifecycle.Observer {  })
        //Observes Discharge Type
        viewModel.dischargeType.observe(
            viewLifecycleOwner,
            { number ->
                if (number != 2) {
                    binding.consistGroup.visibility = View.INVISIBLE
                } else {
                    binding.consistGroup.visibility = View.VISIBLE
                }
            }
        )

        //Discharge Type Button - Sets Discharge
        binding.dischargeButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                //Changes value of dischargeType to 1 or 2
                when (checkedId) {
                    R.id.numberOneButton -> viewModel.onSetDischargeType(1)//set LiveData to 1
                    R.id.numberTwoButton -> viewModel.onSetDischargeType(2) //_dischargeType.value = 2//set LiveData to 2
                }
                //checks value type
                val dischargeToast = viewModel.dischargeType.value.toString()
                showToast(dischargeToast)
            }
        }

        //Duration/Timer Button
        binding.durationInput.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val time = binding.durationInput.text
                    viewModel.onSetDischargeDuration(time.toString())
                    false
                }
                else -> { true }
            }
        }

        //Leakage Button
        binding.leakageYesNoToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.noButton -> viewModel.onSetLeakageYN(false)
                    R.id.yesButton -> viewModel.onSetLeakageYN(true)
                }
                val leakToast = viewModel.leakageYN.value.toString()
                showToast(leakToast)
            }
        }

        //Color Button, changes colorset depending on discharge type
        binding.colorButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            //val dischargeType = viewModel.dischargeType.value
            if (viewModel.dischargeType.value == 1) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.color1Button -> viewModel.onSetDischargeColor(1)
                        R.id.color2Button -> viewModel.onSetDischargeColor(2)
                        R.id.color3Button -> viewModel.onSetDischargeColor(3)
                        R.id.color4Button -> viewModel.onSetDischargeColor(4)
                    }
                    val colorToast = viewModel.convertedColor.value
                    showToast(colorToast)
                }
            } else if (viewModel.dischargeType.value == 2) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.color1Button -> viewModel.onSetDischargeColor(5)
                        R.id.color2Button -> viewModel.onSetDischargeColor(6)
                        R.id.color3Button -> viewModel.onSetDischargeColor(7)
                        R.id.color4Button -> viewModel.onSetDischargeColor(8)
                    }
                    val colorToast = viewModel.convertedColor.value
                    showToast(colorToast)
                }
            }
        }

        //Consistency Button, set to N/A if dischargeType != 2
        binding.consistButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            //val dischargeType = viewModel.dischargeType.value
            if (viewModel.dischargeType.value == 2) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.consist1Button -> viewModel.onSetDischargeConsist("Diarrhea")
                        R.id.consist2Button -> viewModel.onSetDischargeConsist("Loose")
                        R.id.consist3Button -> viewModel.onSetDischargeConsist("Normal")
                        R.id.consist4Button -> viewModel.onSetDischargeConsist("Lumps")
                    }
                    showToast(viewModel.dischargeConsist.value)
                }
            } else {
                viewModel.onSetDischargeConsist("N/A")
            }
        }

        //Submit Button
        binding.submitButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_discharge_fragment_to_discharge_diary_fragment)

            //String Concat
            val typeString = viewModel.dischargeType.value
            val durationString = viewModel.dischargeDurationTime.value
            val leakageString = viewModel.leakageYN.value
            val colorString = viewModel.convertedColor.value
            val consistString = viewModel.dischargeConsist.value

//            val stringBuilder = StringBuilder()
            val dischargeAllInfo = StringBuilder()
                .append(typeString)
                .append(",")
                .append(durationString)
                .append(",")
                .append(leakageString)
                .append(",")
                .append(colorString)
                .append(",")
                .append(consistString)
            showToastLong(dischargeAllInfo.toString())
        }

        return binding.root
    }
    fun showToast(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setNewDateTime(): String? {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        var dateString = ""
        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                val formatter = SimpleDateFormat("EEEE,  MMMM dd ''yy \n hh:mm a", Locale.getDefault())
                dateString = formatter.format(pickedDateTime.time)
                viewModel.getNewDateTime(dateString)
                showToastLong(viewModel.dischargeDateTime.value)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
        return dateString
    }
}
