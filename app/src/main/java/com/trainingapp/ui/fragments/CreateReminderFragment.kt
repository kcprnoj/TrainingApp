package com.trainingapp.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.main.R
import com.example.main.databinding.FragmentCreateReminderBinding



class CreateReminderFragment : Fragment() {

    private lateinit var binding: FragmentCreateReminderBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_reminder,container,false)
        binding.pickTimeButton.setOnClickListener {
            val startTimePicker = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                binding.timeInputLayout.hint = "$hourOfDay:$minute"
            }, 12,0,true)
            startTimePicker.show();
        }
        binding.pickDateButton.setOnClickListener{
            val startDatePicker = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val formatMonth = if(month < 9) "0"+(month+1) else (month+1)
                    val formatDay = if(dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
                binding.pickDateTextInputLayout.hint = "$dayOfMonth.$formatMonth.$year"
            },2021,0,1)
            startDatePicker.show()
        }

        return binding.root
    }
}