package com.trainingapp.ui.fragments

import android.app.AlarmManager
import android.app.AlarmManager.RTC
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.main.R
import com.example.main.databinding.FragmentCreateReminderBinding
import com.trainingapp.utility.AlarmReceiver
import com.trainingapp.viewmodels.CreateReminderViewModel
import com.trainingapp.viewmodels.CreateReminderViewModelFactory
import java.util.*


class CreateReminderFragment : Fragment() {

    private lateinit var binding: FragmentCreateReminderBinding
    private lateinit var viewModel : CreateReminderViewModel
    private lateinit var viewModelFactory : CreateReminderViewModelFactory
    private var calendar = Calendar.getInstance()
    private var defaultHour = 12
    private var defaultMinute = 0
    private var defaultYear = calendar.get(Calendar.YEAR)
    private var defaultMonth = calendar.get(Calendar.MONTH)
    private var defaultDay = calendar.get(Calendar.HOUR_OF_DAY)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_reminder,container,false)
        viewModelFactory = CreateReminderViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateReminderViewModel::class.java)

        binding.pickTimeButton.setOnClickListener {
            val startTimePicker = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val stringHour =
                        if (hourOfDay >= 10)
                            hourOfDay.toString()
                        else
                            "0$hourOfDay"

                val stringMinute =
                        if (minute >= 10)
                            minute.toString()
                        else
                            "0$minute"

                defaultHour = hourOfDay
                defaultMinute = minute

                calendar.set(
                        defaultYear,
                        defaultMonth,
                        defaultDay,
                        defaultHour,
                        defaultMinute,
                )
                binding.timeTextView.text = "$stringHour:$stringMinute"
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),true)
            startTimePicker.show();
        }

        binding.pickDateButton.setOnClickListener{
            val startDatePicker = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val stringMonth =
                        if (month+1 >= 10)
                            (month+1).toString()
                        else
                            "0${month+1}"

                val stringDay =
                        if (dayOfMonth >= 10)
                            dayOfMonth.toString()
                        else
                            "0$dayOfMonth"

                defaultYear = year
                defaultMonth = month
                defaultDay = dayOfMonth

                calendar.set(
                        defaultYear,
                        defaultMonth,
                        defaultDay,
                        defaultHour,
                        defaultMinute,
                        0
                )

                binding.dateTextView.text = "$year.$stringMonth.$stringDay"
            }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            startDatePicker.show()
        }

        binding.createNotificationButton.setOnClickListener{
            setAlarm(calendar.timeInMillis)
        }

        return binding.root
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), AlarmReceiver::class.java)
        intent.putExtra("title", getString(R.string.alarm))
        intent.putExtra("text", getString(R.string.alarm_notification))
        val pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

        Log.d("alarm_done", "alarm_set")
        Toast.makeText(requireActivity(), "Alarm set on ${Date(timeInMillis)}", Toast.LENGTH_SHORT).show()
    }
}