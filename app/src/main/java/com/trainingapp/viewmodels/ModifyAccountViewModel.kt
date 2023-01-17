package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.UserUpdate
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.repository.UserRepository

class ModifyAccountViewModel(private val repository: UserRepository, private val preferences: PrefRepository): ViewModel() {

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess : LiveData<Boolean>
        get() = _updateSuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess : LiveData<Boolean>
        get() = _deleteSuccess

    fun modifyUser(userUpdate: UserUpdate) {
        val response = repository.updateUser(preferences.getUsername(), preferences.getAuthorizationKey(),userUpdate)
        _updateSuccess.value = response.isSuccessful
    }

    fun deleteUser(){
        val response = repository.deleteUser(preferences.getUsername(), preferences.getAuthorizationKey())
        _deleteSuccess.value = response.isSuccessful
    }
}