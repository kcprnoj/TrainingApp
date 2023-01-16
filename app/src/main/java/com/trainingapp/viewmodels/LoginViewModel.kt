package com.trainingapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.trainingapp.model.data.UserLogin
import com.trainingapp.model.data.UserView
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.webservice.UserService

class LoginViewModel(private val service: UserService, private val repository: PrefRepository) : ViewModel() {

    private val gson = Gson()

    fun login(user: UserLogin): Boolean {
        val response = service.login(user)

        if (response.isSuccessful) {
            repository.saveAuthorizationKey(response.headers("Authorization")[0].toString())
            repository.saveUsername(user.username)
            repository.saveUser(service.getUser(user.username, repository.getAuthorizationKey()))
            return true
        }

        return false
    }

    fun cleanRepo() {
        repository.saveUser(UserView("","","","",""))
        repository.saveAuthorizationKey("")
        repository.saveUsername("")
    }
}