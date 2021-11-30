package com.example.dischargediary.discharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeBinding

class DischargeFragment : Fragment() {

    private lateinit var viewModel: DischargeViewModel
    //private lateinit var viewModelFactory: DischargeViewModelFactory

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

        binding.submitButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_discharge_fragment_to_discharge_diary_fragment)
        }

        //Discharge Type Button
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
            val dischargeType = viewModel.dischargeType.value
            if (dischargeType == 1) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.color1Button -> viewModel.onSetDischargeColor(1)
                        R.id.color2Button -> viewModel.onSetDischargeColor(2)
                        R.id.color3Button -> viewModel.onSetDischargeColor(3)
                        R.id.color4Button -> viewModel.onSetDischargeColor(4)
                    }
                    val colorToast = colorConverter(viewModel.dischargeColor.value)
                    showToast(colorToast.toString())
                }
            } else if (dischargeType == 2) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.color1Button -> viewModel.onSetDischargeColor(5)
                        R.id.color2Button -> viewModel.onSetDischargeColor(6)
                        R.id.color3Button -> viewModel.onSetDischargeColor(7)
                        R.id.color4Button -> viewModel.onSetDischargeColor(8)
                    }
                    val colorToast = colorConverter(viewModel.dischargeColor.value)
                    showToast(colorToast)
                }
            }
        }

        //Consistency Button, set to N/A if dischargeType != 2
        binding.consistButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            val dischargeType = viewModel.dischargeType.value
            if (dischargeType == 2) {
                if (isChecked) {
                    when (checkedId) {
                        R.id.consist1Button -> viewModel.onSetDischargeConsist("Diarrhea")
                        R.id.consist2Button -> viewModel.onSetDischargeConsist("Loose")
                        R.id.consist3Button -> viewModel.onSetDischargeConsist("Normal")
                        R.id.consist4Button -> viewModel.onSetDischargeConsist("Lumps")
                    }
                }
            } else {
                viewModel.onSetDischargeConsist("N/A")
                //add function to hide if dischargeType == 1
            }
        }
        return binding.root
    }
    fun showToast(str: String) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }
    fun colorConverter(colorCode: Int?): String {
        val colorName = when (colorCode) {
            0 -> "Need Value"
            1 -> "Clear"
            2 -> "Light Yellow"
            3 -> "Yellow"
            4 -> "Dark Yellow"
            5 -> "Light Brown"
            6 -> "Brown"
            7 -> "Green"
            8 -> "Black"
            else -> { "Other" }
        }
        return colorName
    }
}
