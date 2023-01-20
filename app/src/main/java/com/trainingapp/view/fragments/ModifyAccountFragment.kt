package com.trainingapp.view.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentModifyAccountBinding
import com.trainingapp.RunApplication
import com.trainingapp.model.repository.UserRepository
import com.trainingapp.model.webservice.UserService
import com.trainingapp.viewmodels.ModifyAccountViewModel
import com.trainingapp.viewmodels.ModifyAccountViewModelFactory
import java.util.*

class ModifyAccountFragment : Fragment() {
    private lateinit var binding: FragmentModifyAccountBinding
    private lateinit var viewModelFactory: ModifyAccountViewModelFactory
    private lateinit var viewModel: ModifyAccountViewModel

    private val CHANNEL_ID = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_account, container, false)
        viewModelFactory = ModifyAccountViewModelFactory(UserRepository(UserService()), (requireActivity().application as RunApplication).perfRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ModifyAccountViewModel::class.java)
        binding.viewModel = viewModel
        setClientObservers()
        setListeners()
        createAutoComplete()

        return binding.root
    }

    private fun setListeners(){
        binding.backButton.setOnClickListener{
            findNavController().navigate(ModifyAccountFragmentDirections.actionModifyAccountFragmentToTrainingFragment())
        }

        binding.deleteAccountButton.setOnClickListener{
            viewModel.deleteUser()
        }

        binding.updateButton.setOnClickListener{
            modifyAccount()
        }

        binding.birthdayInput.setOnClickListener {
            createDatePicker()
        }
    }

    private fun modifyAccount(){
        var newSex = binding.sexAutoComplete.text.toString()
        val newWeight = binding.weightInput.text.toString().toDouble()
        val newHeight = binding.heightInput.text.toString().toDouble()
        val newBirthday = binding.birthdayInput.text.toString()
        newSex = when(newSex) {
            resources.getString(R.string.male) -> "male"
            resources.getString(R.string.female) -> "female"
            resources.getString(R.string.other) -> "other"
            else -> ""
        }
        viewModel.modifyUser(newSex, newWeight,newHeight, newBirthday)
    }

    private fun createDatePicker(){
        val calendar = Calendar.getInstance()
        val yearCal = calendar.get(Calendar.YEAR)
        val monthCal = calendar.get(Calendar.MONTH)
        val dayCal = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
            val stringMonth = formatMonth(month)
            val stringDay = formatDay(dayOfMonth)

            val dat = "$year-$stringMonth-$stringDay"
            binding.birthdayInput.setText(dat)
        }, yearCal,monthCal,dayCal)
        datePickerDialog.show()
    }

    private fun createAutoComplete(){
        val items = listOf(resources.getString(R.string.male), resources.getString(R.string.female), resources.getString(R.string.other))
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.sexAutoComplete.setAdapter(adapter)
        val default = when(viewModel.sex.value) {
            "male" -> resources.getString(R.string.male)
            "female" -> resources.getString(R.string.female)
            "other" -> resources.getString(R.string.other)
            else -> ""
        }
        binding.sexAutoComplete.setText(default, false)
    }

    private fun setClientObservers() {
        viewModel.modifySuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(ModifyAccountFragmentDirections.actionModifyAccountFragmentToTrainingFragment())
            } else {
                    Toast.makeText(requireActivity(), getString(R.string.failed_modify), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.deleteSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(ModifyAccountFragmentDirections.actionModifyAccountFragmentToLoginFragment())
                notifyDelete()
            } else {
                    Toast.makeText(requireActivity(), getString(R.string.failed_delete), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun notifyDelete() {
        Log.d("notify", "delete")

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_directions_run_24)
                .setContentTitle(getString(R.string.success))
                .setContentText(getString(R.string.notification_delete))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(0, builder.build())
        }
    }

    fun formatDay(dayOfMonth: Int): String{
        return if (dayOfMonth >= 10)
            dayOfMonth.toString()
        else
            "0$dayOfMonth"
    }

    fun formatMonth(month: Int): String{
        return if (month+1 >= 10)
            (month+1).toString()
        else
            "0${month+1}"
    }
}