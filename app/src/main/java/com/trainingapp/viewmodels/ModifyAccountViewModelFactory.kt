package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.repository.UserRepository

class ModifyAccountViewModelFactory(private val repository: UserRepository, private val preferences: PrefRepository) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((ModifyAccountViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return ModifyAccountViewModel(repository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}