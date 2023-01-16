package com.trainingapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.webservice.UserService

class LoginViewModelFactory (private val service: UserService, private val repository: PrefRepository) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((LoginViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(service, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}