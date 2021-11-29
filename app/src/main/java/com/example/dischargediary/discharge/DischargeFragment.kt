package com.example.dischargediary.discharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.dischargediary.R
import com.example.dischargediary.databinding.FragmentDischargeBinding

class DischargeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDischargeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_discharge, container, false)
        binding.submitButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_discharge_fragment_to_discharge_diary_fragment)
        }
        binding.dischargeButtonToggleGroup.addOnButtonCheckedListener { _, checkedType, isChecked ->
            if (isChecked) {
                //Change value of dischargeType to 1 or 2
                when (checkedType) {
                    R.id.numberOneButton -> showToast("One")//set LiveData to 1
                    R.id.numberTwoButton -> showToast("Two")//_dischargeType.value = 2//set LiveData to 2
                }
            }
        }
        return binding.root
    }
    private fun showToast(str: String) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show()
    }
}
