package com.trainingapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.main.R

import com.example.main.databinding.FragmentRegisterBinding
import com.trainingapp.ui.MainActivity

import com.trainingapp.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.username_text_input_layout
import org.json.JSONObject


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this

        (activity as MainActivity).stompClient.topic("/user/queue/register").subscribe( { topicMessage ->
            val reply = JSONObject(topicMessage.payload)
            when {
                reply.getString("Successful") == "True" -> {
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                }
                else -> {
                    Log.d("Login", "Failed")
                }
            }
        }, {})

        binding.registerButton.setOnClickListener {
            val username = username_text_input_layout.editText?.text.toString()
            val password = password_text_input_layout.editText?.text.toString()
            val weight = weight_text_input_layout.editText?.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("login", username)
            jsonObject.put("password", password)
            jsonObject.put("weight", weight)

            (activity as MainActivity).stompClient.send("/app/register",  jsonObject.toString()).subscribe({ }, {
                Log.d("Login", "Server Error")
            })

        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        return binding.root
    }
}