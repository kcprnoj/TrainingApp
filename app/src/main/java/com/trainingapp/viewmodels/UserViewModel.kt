package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trainingapp.model.webservice.UserService
import com.trainingapp.model.data.UserLogin

class UserViewModel(private val service: UserService) : ViewModel() {
    fun login(user: UserLogin): String? {
        val response = service.login(user)

        if (response.isSuccessful) {
            return response.headers("Authorization")[0].toString()
        }

        return null
    }
}