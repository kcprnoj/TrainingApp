package com.trainingapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.UserUpdate
import com.trainingapp.model.data.UserView
import com.trainingapp.model.repository.PreferencesRepository
import com.trainingapp.model.repository.UserRepository

class ModifyAccountViewModel(private val repository: UserRepository, private val preferences: PreferencesRepository): ViewModel() {

    private val _modifySuccess = MutableLiveData<Boolean>()
    val modifySuccess : LiveData<Boolean>
        get() = _modifySuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess : LiveData<Boolean>
        get() = _deleteSuccess

    private val _sex = MutableLiveData<String>()
    val sex : LiveData<String>
        get() = _sex

    private val _weightString = MutableLiveData<String>()
    val weightString : LiveData<String>
        get() = _weightString

    private val _heightString = MutableLiveData<String>()
    val heightString : LiveData<String>
        get() = _heightString

    private val _birthday = MutableLiveData<String>()
    val birthday : LiveData<String>
        get() = _birthday

    init {
        getUserDetails()
    }

    fun modifyUser(sex:String, weight:Double, height:Double, birthday:String) {
        val user = UserUpdate(sex, weight, height, birthday)
        val success = repository.updateUser(preferences.getUsername(), preferences.getAuthorizationKey(), user)
        _modifySuccess.value = success
    }

    fun deleteUser(){
        val success = repository.deleteUser(preferences.getUsername(), preferences.getAuthorizationKey())
        _deleteSuccess.value = success
    }

    fun getUserDetails(){
        val user = repository.getUser(preferences.getUsername(), preferences.getAuthorizationKey())
        _sex.value = user.sex
        _birthday.value = user.birthday
        _weightString.value = user.weight
        _heightString.value = user.height
    }
}