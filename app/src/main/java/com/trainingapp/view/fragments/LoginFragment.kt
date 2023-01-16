package com.trainingapp.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentLoginBinding
import com.trainingapp.model.webservice.UserService
import com.trainingapp.model.data.UserLogin
import com.trainingapp.view.MainActivity
import com.trainingapp.viewmodels.UserViewModel
import com.trainingapp.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.username_text_input_layout


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModelFactory: UserViewModelFactory
    private lateinit var viewModel: UserViewModel

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModelFactory = UserViewModelFactory(UserService())
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
        (activity as MainActivity).saveAuthorizationKey("")
        (activity as MainActivity).saveUsername("")

        binding.loginButton.setOnClickListener {

            val username = username_text_input_layout.editText?.text.toString()
            val password = password_text_input.editText?.text.toString()

            val user = UserLogin(username, password)
            val key = viewModel.login(user)

            if (key != null) {
                Log.d("Login", "Logged in $key")
                (activity as MainActivity).saveAuthorizationKey(key)
                (activity as MainActivity).saveUsername(username)
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTrainingFragment())
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.failed_login),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        return binding.root
    }


}