package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.UserLogin
import com.trainingapp.model.data.UserView
import com.trainingapp.model.repository.PreferencesRepository
import com.trainingapp.model.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository, private val perfRepository: PreferencesRepository) : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess : LiveData<Boolean>
        get() = _loginSuccess

    fun login(user: UserLogin) {
        val key = userRepository.login(user)

        if (key != null) {
            perfRepository.saveAuthorizationKey(key)
            perfRepository.saveUsername(user.username)
            userRepository.getUser(user.username, perfRepository.getAuthorizationKey())
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