package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.repository.PreferencesRepository
import com.trainingapp.model.repository.UserRepository

class LoginViewModelFactory (private val userRepository: UserRepository, private val perfRepository: PreferencesRepository) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((LoginViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository, perfRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}