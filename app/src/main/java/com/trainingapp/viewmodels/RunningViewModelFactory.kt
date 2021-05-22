package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.repositories.RunRepository

class RunningViewModelFactory(private val repository: RunRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((RunningViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return RunningViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}