package com.trainingapp.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentLoginBinding
import com.trainingapp.RunApplication
import com.trainingapp.model.webservice.UserService
import com.trainingapp.model.data.UserLogin
import com.trainingapp.model.repository.UserRepository
import com.trainingapp.viewmodels.LoginViewModel
import com.trainingapp.viewmodels.LoginViewModelFactory


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModelFactory: LoginViewModelFactory
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModelFactory = LoginViewModelFactory(UserRepository(UserService()),
            (requireActivity().application as RunApplication).perfRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        viewModel.cleanRepo()

        setClientObservers()
        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.loginButton.setOnClickListener {

            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val user = UserLogin(username, password)
            viewModel.login(user)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

    }

    private fun setClientObservers() {
        viewModel.loginSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTrainingFragment())
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.failed_login),
                    Toast.LENGTH_SHORT
                ).show()            }
        })
    }
}