package com.trainingapp.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.trainingapp.model.data.UserLogin
import com.trainingapp.model.data.UserView
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.repository.UserRepository
import com.trainingapp.model.webservice.UserService

class LoginViewModel(private val userRepository: UserRepository, private val perfRepository: PrefRepository) : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess : LiveData<Boolean>
        get() = _loginSuccess

    fun login(user: UserLogin) {
        val key = userRepository.login(user)

        if (key != null) {
            perfRepository.saveAuthorizationKey(key)
            perfRepository.saveUsername(user.username)
            userRepository.getUser(user.username)
                .let { perfRepository.saveUser(it) }
            _loginSuccess.value = true
            return
        }

        _loginSuccess.value = false
    }

    fun cleanRepo() {
        perfRepository.saveUser(UserView("","","","",""))
        perfRepository.saveAuthorizationKey("")
        perfRepository.saveUsername("")
    }
}