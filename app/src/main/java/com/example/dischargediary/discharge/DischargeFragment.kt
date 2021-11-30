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

        binding.dischargeButtonToggleGroup.addOnButtonCheckedListener { _, checkedType, isChecked ->
            if (isChecked) {
                //Changes value of dischargeType to 1 or 2
                when (checkedType) {
                    R.id.numberOneButton -> viewModel.onSetDischargeType(1)//set LiveData to 1
                    R.id.numberTwoButton -> viewModel.onSetDischargeType(2) //_dischargeType.value = 2//set LiveData to 2
                }
                //checks value type
                val toast = viewModel.dischargeType.value.toString()
                showToast(toast)
            }
        }
        return binding.root
    }
    fun showToast(str: String) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }
}
