package com.trainingapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.repository.UserRepository
import com.trainingapp.model.webservice.UserService

class LoginViewModelFactory (private val userRepository: UserRepository, private val perfRepository: PrefRepository) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((LoginViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository, perfRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}