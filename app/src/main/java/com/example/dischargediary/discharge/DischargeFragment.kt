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
        val viewModelFactory = DischargeViewModelFactory(arguments.dischargeTypeArg, datasource)
        Log.d("CheckDischargeFrag", "${arguments.dischargeTypeArg}")
        dischargeViewModel = ViewModelProvider(this, viewModelFactory).get(DischargeViewModel::class.java)

        binding.dischargeViewModel = dischargeViewModel
        //binding.lifecycleOwner = this

        //Current Date & Time
        binding.dischargeDateTime.setOnClickListener { setNewDateTime() }
        //Observes Discharge Type & sets discharge colors accordingly
        dischargeViewModel.dischargeType.observe(
            viewLifecycleOwner, { number ->
                if (number != 2) {
                    showNumberOneUi(binding)
                    dischargeViewModel.onSetDischargeColor(dischargeViewModel.dischargeColorButton.value)
                    dischargeViewModel.onSetDischargeConsist("N/A")
                } else {
                    showNumberTwoUi(binding)
                    dischargeViewModel.onSetDischargeColor(dischargeViewModel.dischargeColorButton.value)
                }
            }
        )
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
                val colorString = dischargeViewModel.dischargeColor.value
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
    private fun showNumberOneUi(binding: FragmentDischargeBinding) {
        binding.apply {
            consistGroup.visibility = View.INVISIBLE
            dischargeButtonToggleGroup.check(R.id.numberOneButton)
            color1Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.clear_urine))
            color2Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.light_yellow_urine))
            color3Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.yellow_urine))
            color4Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.dark_yellow_urine))
            color5Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.red_urine))
        }
    }
    private fun showNumberTwoUi(binding: FragmentDischargeBinding) {
        binding.apply {
            consistGroup.visibility = View.VISIBLE
            dischargeButtonToggleGroup.check(R.id.numberTwoButton)
            color1Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_stool))
            color2Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.brown_stool))
            color3Button.setBackgroundColor(ContextCompat.getColor(context!!,R.color.dark_brown_black_stool))
            color4Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.green_stool))
            color5Button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.red_stool))
        }
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
