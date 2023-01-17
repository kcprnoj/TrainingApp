package com.trainingapp.view.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentRegisterBinding
import com.trainingapp.model.data.UserRegister
import com.trainingapp.model.repository.UserRepository
import com.trainingapp.model.webservice.UserService
import com.trainingapp.viewmodels.RegisterViewModel
import com.trainingapp.viewmodels.RegisterViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModelFactory: RegisterViewModelFactory
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        viewModelFactory = RegisterViewModelFactory(UserRepository(UserService()))
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

        setClientObservers()
        setListeners()
        createAutoComplete()

        return binding.root
    }

    private fun setListeners() {
        binding.registerButton.setOnClickListener {
            userRegister()
        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        binding.birthdayInput.setOnClickListener {
            createDatePicker()
        }
    }

    private fun userRegister(){
        val username = binding.usernameInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()
        val weight = binding.weightInput.text.toString().toDouble()
        val height = binding.heightInput.text.toString().toDouble()
        var sex = binding.sexAutoComplete.text.toString()
        val birthday = binding.birthdayInput.text.toString()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val birthdayFormatted = LocalDate.parse(birthday, dateFormatter)

        sex = when (sex) {
            resources.getString(R.string.male) -> {
                "male"
            }
            resources.getString(R.string.female) -> {
                "female"
            }
            else -> {
                "other"
            }
        }

        val user = UserRegister(username, password, sex, weight, height, birthdayFormatted)
        viewModel.registerUser(user)
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
    }

    private fun setClientObservers() {
        viewModel.registerSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                Toast.makeText(requireActivity(), getString(R.string.success_register), Toast.LENGTH_SHORT).show()
            } else {
                    Toast.makeText(requireActivity(), getString(R.string.failed_register), Toast.LENGTH_SHORT).show()
            }
        })
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