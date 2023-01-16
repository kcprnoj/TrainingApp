package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.webservice.UserService

class RegisterViewModelFactory (private val service: UserService) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((UserViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}