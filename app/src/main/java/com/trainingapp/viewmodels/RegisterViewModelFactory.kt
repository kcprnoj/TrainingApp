package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.webservice.UserService

class RegisterViewModelFactory (private val service: UserService) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((RegisterViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}