package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.UserUpdate
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.repository.UserRepository

class ModifyAccountViewModel(private val repository: UserRepository, private val preferences: PrefRepository): ViewModel() {

    private val _modifySuccess = MutableLiveData<Boolean>()
    val modifySuccess : LiveData<Boolean>
        get() = _modifySuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess : LiveData<Boolean>
        get() = _deleteSuccess

    private val _previousSex = MutableLiveData<Boolean>()
    val previousSex : LiveData<Boolean>
        get() = _previousSex

    private val _previousWeight = MutableLiveData<Boolean>()
    val previousWeight : LiveData<Boolean>
        get() = _previousWeight

    private val _previousHeight = MutableLiveData<Boolean>()
    val previousHeight : LiveData<Boolean>
        get() = _previousHeight

    private val _previousBirthday = MutableLiveData<Boolean>()
    val previousBirthday : LiveData<Boolean>
        get() = _previousBirthday

    fun modifyUser(userUpdate: UserUpdate) {
        val response = repository.updateUser(preferences.getUsername(), userUpdate)
        _modifySuccess.value = response.isSuccessful
    }

    fun deleteUser(){
        val response = repository.deleteUser(preferences.getUsername())
        _deleteSuccess.value = response.isSuccessful
    }

    fun getUserDetails(){

    }
}