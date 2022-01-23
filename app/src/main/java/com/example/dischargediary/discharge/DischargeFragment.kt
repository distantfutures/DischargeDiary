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
import com.example.dischargediary.data.DischargeDatabase
import com.example.dischargediary.databinding.FragmentDischargeBinding
import java.text.SimpleDateFormat
import java.util.*

class DischargeFragment : Fragment() {

    private lateinit var dischargeViewModel: DischargeViewModel
    //private lateinit var viewModelFactory: DischargeViewModelFactory

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

        val datasource = DischargeDatabase.getInstance(application).dischargeDatabaseDao
        val viewModelFactory = DischargeViewModelFactory(arguments.entryIdKey, datasource)
        Log.d("DischargeFrag", "${arguments.entryIdKey}")
        dischargeViewModel = ViewModelProvider(this, viewModelFactory).get(DischargeViewModel::class.java)

        binding.dischargeViewModel = dischargeViewModel
        //binding.lifecycleOwner = this

        //Current Date & Time
        binding.dischargeDateTime.setOnClickListener { setNewDateTime() }

        //Observes Discharge Type & sets discharge colors accordingly
        dischargeViewModel.dischargeType.observe(
            viewLifecycleOwner,
            { number ->
                if (number != 2) {
                    binding.consistGroup.visibility = View.INVISIBLE
                    binding.dischargeButtonToggleGroup.check(R.id.numberOneButton)

                    binding.color1Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.clear_urine))
                    binding.color2Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.light_yellow_urine))
                    binding.color3Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.yellow_urine))
                    binding.color4Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.dark_yellow_urine))
                    binding.color5Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.red_urine))
                } else {
                    binding.consistGroup.visibility = View.VISIBLE
                    binding.dischargeButtonToggleGroup.check(R.id.numberTwoButton)

                    binding.color1Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_stool))
                    binding.color2Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.brown_stool))
                    binding.color3Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.dark_brown_black_stool))
                    binding.color4Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.green_stool))
                    binding.color5Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.red_stool))
                }
            }
        )

        //Discharge Type Button - Sets Discharge
        binding.dischargeButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                //Changes value of dischargeType to 1 or 2
                when (checkedId) {
                    R.id.numberOneButton -> dischargeViewModel.onSetDischargeType(1)//set LiveData to 1
                    R.id.numberTwoButton -> dischargeViewModel.onSetDischargeType(2) //_dischargeType.value = 2//set LiveData to 2
                }
                //checks value type
                val dischargeToast = dischargeViewModel.dischargeType.value.toString()
                showToast(dischargeToast)
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

        //Leakage Button
        binding.leakageYesNoToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.noButton -> dischargeViewModel.onSetLeakageYN(false)
                    R.id.yesButton -> dischargeViewModel.onSetLeakageYN(true)
                }
                val leakToast = dischargeViewModel.leakageYN.value.toString()
                showToast(leakToast)
            }
        }

        //Color Button, changes colorset depending on discharge type
        binding.colorButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            //val dischargeType = viewModel.dischargeType.value
            if (dischargeViewModel.dischargeType.value == 1) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.color1Button -> dischargeViewModel.onSetDischargeColor(1)
                        R.id.color2Button -> dischargeViewModel.onSetDischargeColor(2)
                        R.id.color3Button -> dischargeViewModel.onSetDischargeColor(3)
                        R.id.color4Button -> dischargeViewModel.onSetDischargeColor(4)
                        R.id.color5Button -> dischargeViewModel.onSetDischargeColor(5)
                    }
                    val colorToast = dischargeViewModel.convertedColor.value
                    showToast(colorToast)
                }
            } else if (dischargeViewModel.dischargeType.value == 2) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.color1Button -> dischargeViewModel.onSetDischargeColor(6)
                        R.id.color2Button -> dischargeViewModel.onSetDischargeColor(7)
                        R.id.color3Button -> dischargeViewModel.onSetDischargeColor(8)
                        R.id.color4Button -> dischargeViewModel.onSetDischargeColor(9)
                        R.id.color5Button -> dischargeViewModel.onSetDischargeColor(10)
                    }
                    val colorToast = dischargeViewModel.convertedColor.value
                    showToast(colorToast)
                }
            }
        }

        //Consistency Button, set to N/A if dischargeType != 2
        binding.consistButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            //val dischargeType = viewModel.dischargeType.value
            if (dischargeViewModel.dischargeType.value == 2) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.consist1Button -> dischargeViewModel.onSetDischargeConsist("Severe Constipation")
                        R.id.consist2Button -> dischargeViewModel.onSetDischargeConsist("Mild Constipation")
                        R.id.consist3Button -> dischargeViewModel.onSetDischargeConsist("Normal, Dehydrated")
                        R.id.consist4Button -> dischargeViewModel.onSetDischargeConsist("Normal")
                        R.id.consist5Button -> dischargeViewModel.onSetDischargeConsist("Lacking Fiber")
                        R.id.consist6Button -> dischargeViewModel.onSetDischargeConsist("Diarrhea")
                    }
                    showToast(dischargeViewModel.dischargeConsist.value)
                }
            } else {
                dischargeViewModel.onSetDischargeConsist("N/A")
            }
        }

        //Submit Button
        dischargeViewModel.navigateToDiary.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                this.findNavController().navigate(DischargeFragmentDirections.actionDischargeFragmentToDischargeDiaryFragment())
                dischargeViewModel.doneNavigating()

                //Toast
                val dateString = dischargeViewModel.dischargeDate.value
                val timeString = dischargeViewModel.dischargeTime.value
                val typeString = dischargeViewModel.dischargeType.value
                val durationString = dischargeViewModel.dischargeDurationTime.value
                val leakageString = dischargeViewModel.leakageYN.value
                val colorString = dischargeViewModel.convertedColor.value
                val consistString = dischargeViewModel.dischargeConsist.value

                val dischargeAllInfo = StringBuilder()
                    .append("$dateString - $timeString")
                    .append(",")
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
            } else {
                showToast("Entry Incomplete")
            }
        })
        return binding.root
    }
    fun showToast(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(str: String?) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun setNewDateTime(): String? {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)
        var dateString = ""
        var timeString = ""
        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                //format Date
                val formatterDate = SimpleDateFormat("MM.dd.yyyy, EEE", Locale.getDefault())
                dateString = formatterDate.format(pickedDateTime.time)
                //format Time
                val formatterTime = SimpleDateFormat("h:mm a", Locale.getDefault())
                timeString = formatterTime.format(pickedDateTime.time)
                //set Date & Time to ViewModel
                dischargeViewModel.getNewDate(dateString)
                dischargeViewModel.getNewTime(timeString)
                //Toast Info
                showToast(dischargeViewModel.dischargeDate.value)
                showToast(dischargeViewModel.dischargeTime.value)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
        return dateString
    }
}
