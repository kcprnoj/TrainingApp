package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.UserRegister
import com.trainingapp.model.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess : LiveData<Boolean>
        get() = _registerSuccess

    fun registerUser(userRegister: UserRegister) {
       val response = repository.registerUser(userRegister)
        _registerSuccess.value = response.isSuccessful
    }

}