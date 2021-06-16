package com.trainingapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentRegisterBinding
import com.trainingapp.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.username_text_input_layout
import org.json.JSONObject


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private var skip = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this

        setClientObservers()
        setListeners()

        val items = listOf(resources.getString(R.string.male), resources.getString(R.string.female), resources.getString(R.string.other))
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.sexAuto.setAdapter(adapter)

        return binding.root
    }

    private fun setListeners() {
        binding.registerButton.setOnClickListener {
            val username = username_text_input_layout.editText?.text.toString()
            val password = password_text_input_layout.editText?.text.toString()
            val weight = weight_text_input_layout.editText?.text.toString()
            val height = height_text_input_layout.editText?.text.toString()
            var sex = sex_text_input_layout.editText?.text.toString()

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

            val jsonObject = JSONObject()
            jsonObject.put("login", username)
            jsonObject.put("password", password)
            jsonObject.put("weight", weight)
            jsonObject.put("height", height)
            jsonObject.put("sex", sex)

            (activity as MainActivity).stompClient.send("/app/register",  jsonObject.toString()).subscribe({ }, {
                Log.d("Login", "Server Error")
            })

        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }


    private fun setClientObservers() {
        (activity as MainActivity).registerSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                skip = true
                (activity as MainActivity).registerSuccess.postValue(false)
            } else {
                if (!skip)
                    Toast.makeText(requireActivity(), getString(R.string.failed_register), Toast.LENGTH_SHORT).show()
                else
                    skip = false
            }
        })
    }
}