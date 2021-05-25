package com.trainingapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentLoginBinding
import com.trainingapp.ui.MainActivity
import com.trainingapp.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.username_text_input_layout
import org.json.JSONObject


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

        //TODO REMOVE WHEN PRODUCTION
        //if (!(activity as MainActivity).stompClient.isConnected) {
       //     findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTrainingFragment())
       // }
/*
        (activity as MainActivity).stompClient.topic("/user/queue/login").subscribe( { topicMessage ->
            val reply = JSONObject(topicMessage.payload)
            when {
                reply.getString("Successful") == "True" -> {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTrainingFragment())
                }
                else -> {
                    Log.d("Login", "Failed")
                }
            }
        }, {})*/

        (activity as MainActivity).loginSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Log.d("Login", "Logged in")
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTrainingFragment())
                (activity as MainActivity).loginSuccess.postValue(false)
            }
        })

        binding.loginButton.setOnClickListener {

            val username = username_text_input_layout.editText?.text.toString()
            val password = password_text_input.editText?.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("login", username)
            jsonObject.put("password", password)

            (activity as MainActivity).stompClient.send("/app/login",  jsonObject.toString()).subscribe({ }, {
                Log.d("Login", "Server Error")
            })

        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        return binding.root
    }


}